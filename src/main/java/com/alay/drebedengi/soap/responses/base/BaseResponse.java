package com.alay.drebedengi.soap.responses.base;

import com.alay.drebedengi.api.DrebedengiServerException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.alay.drebedengi.soap.SoapConstants.ANY_TYPE;
import static com.alay.drebedengi.soap.SoapConstants.ARRAY_TYPE;
import static com.alay.drebedengi.soap.SoapConstants.MAP_TYPE;
import static com.alay.drebedengi.soap.SoapConstants.TYPE_ATTR;

@SuppressWarnings("rawtypes")
public abstract class BaseResponse <T extends BaseResponse> {

    private static final String FAULT_TAG = "SOAP-ENV:Fault";
    private static final String FAULTCODE_TAG = "faultcode";
    private static final String FAULTSTRING_TAG = "faultstring";

    private Document document;

    @SuppressWarnings("unchecked")
    public T init(String soap) {
        document = parseDocument(soap);
        if (isSuccess()) {
            readTags();
        } else {
            throw new DrebedengiServerException(getFailCode(), getFailMessage());
        }
        return (T) this;
    }

    @SneakyThrows
    private static Document parseDocument(String xml) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return dBuilder.parse(is);
    }

    private String getTextContent(String tag) {
        NodeList fault = document.getElementsByTagName(tag);
        return fault.getLength() > 0 ? fault.item(0).getTextContent() : "";
    }

    private static Stream<Node> childNodesAsStream(Node node) {
        NodeList nl = node.getChildNodes();
        return IntStream.range(0, nl.getLength())
                .mapToObj(nl::item);
    }

    protected abstract void readTags();

    protected static String readTagString(Element element) {
        return readTag(element, e -> e.getTextContent().trim());
    }

    protected static String readTagString(Element element, String tagName) {
        return unscape(readTagString(getFirstChild(element, tagName)));
    }

    protected String readTagString(String tagName) {
        return unscape(readTagString(getFirstChild(document.getDocumentElement(), tagName)));
    }

    protected static Integer readTagInteger(Element element) {
        return readTag(element, key -> Integer.valueOf(key.getTextContent().trim()));
    }

    protected Integer readTagInteger(String tagName) {
        return readTagInteger(getFirstChild(document.getDocumentElement(), tagName));
    }

    @SuppressWarnings("SameParameterValue")
    protected static Integer readTagInteger(Element element, String tagName) {
        return readTagInteger(getFirstChild(element, tagName));
    }

    /**
     * @param element     Element with list of key-value items
     * @param keyMapper   extract key from <item><key/><value/></item> node.
     * @param valueMapper extract value from <item><key/><value/></item> node.
     */
    protected static <K, V> Map<K, V> readTagMap(Element element, Function<Element, K> keyMapper, Function<Element, V> valueMapper) {
        checkTagType(element, MAP_TYPE);
        return childNodesAsStream(element)
                .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                .map(n -> (Element) n)
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    @SuppressWarnings("SameParameterValue")
    protected static <K, V> Map<K, V> readTagMap(Element element, String tagName, Function<Element, K> keyMapper, Function<Element, V> valueMapper) {
        return readTagMap(getFirstChild(element, tagName), keyMapper, valueMapper);
    }


    protected <K, V> Map<K, V> readTagMap(String tagName, Function<Element, K> keyMapper, Function<Element, V> valueMapper) {
        return readTagMap(getFirstChild(document, tagName),
                keyMapper,
                valueMapper);
    }

    protected <V> List<V> readTagList(String tagName, Function<Element, V> mapper) {
        return readTagList(getFirstChild(document, tagName), mapper);
    }

    protected static <V> List<V> readTagList(Element element, Function<Element, V> mapper) {
        checkTagType(element, ARRAY_TYPE);
        return childNodesAsStream(element)
                .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                .map(n -> (Element) n)
                .map(mapper)
                .collect(Collectors.toList());
    }

    protected static <V> V readTag(Element element, Function<Element, V> mapper) {
        checkTagType(element, ANY_TYPE);
        return mapper.apply(element);
    }

    private static void checkTagType(Element element, String expectedType) {
        if (StringUtils.isNotEmpty(expectedType) && element.hasChildNodes()) {
            String actualType = element.getAttribute(TYPE_ATTR);
            if (!expectedType.equals(actualType)) {
                throw new IllegalArgumentException(element.getTagName() +
                        " has unexpected type. Expected: " + expectedType + ". Actual: " + actualType);
            }
        }
    }

    private static Element getFirstChild(Document document, String tagName) {
        return getFirstChild(document.getDocumentElement(), tagName);
    }

    private static Element getFirstChild(Element root, String tagName) {
        NodeList nodeList = root.getElementsByTagName(tagName);
        return nodeList.getLength() > 0 ? (Element) nodeList.item(0) : null;
    }

    public boolean isSuccess() {
        NodeList fault = document.getElementsByTagName(FAULT_TAG);
        return fault.getLength() == 0;
    }

    public String getFailMessage() {
        return StringUtils.trimToNull(getTextContent(FAULTSTRING_TAG));
    }

    public Integer getFailCode() {
        String faultCode = getTextContent(FAULTCODE_TAG);
        if (StringUtils.isNotEmpty(faultCode)) {
            return Integer.valueOf(faultCode);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unused")
    public void printDocument() {
        System.out.println(documentToString(document, false));
    }

    @SneakyThrows
    public static String documentToString(Document document, boolean ident) {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, ident ? "yes" : "no");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(document), new StreamResult(out));
        return out.toString();
    }

    // It looks like a bug in the drebedengi API, since string like "H&M" returned from server as double-escaped: "H&amp;amp;M"
    private static String unscape(String str) {
        if (str.contains("&")) {
            return str.replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">");
        }
        return str;
    }

}
