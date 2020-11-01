package com.alay.drebedengi.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class WebClient {

    private static final String REQUEST_TEMPLATE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<SOAP-ENV:Envelope\n" +
                    "        xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                    "        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                    "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "        xmlns:ns1=\"urn:ddengi\"\n" +
                    "        xmlns:ns2=\"http://xml.apache.org/xml-soap\"\n" +
                    "        xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
                    "        SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                    "  <SOAP-ENV:Body>\n" +
                    "    <ns1:__function__>\n" +
                    "      <apiId xsi:type=\"xsd:string\">__api_key__</apiId>\n" +
                    "      <login xsi:type=\"xsd:string\">__login__</login>\n" +
                    "      <pass xsi:type=\"xsd:string\">__pass__</pass>\n" +
                    "      __function_data__\n" +
                    "    </ns1:__function__>\n" +
                    "  </SOAP-ENV:Body>\n" +
                    "</SOAP-ENV:Envelope>";
    private static final String SOAP_MEDIA_TYPE = "application/soap+xml; charset=utf-8";
    private final HttpClient httpClient;
    private final URI drebedengiUri;
    private final String requestTemplate;


    public WebClient(DrebedengiConf conf) {
        this.httpClient = HttpClient.newHttpClient();
        drebedengiUri = conf.getUri();
        this.requestTemplate = REQUEST_TEMPLATE
                .replaceFirst("__api_key__", conf.getApiKey())
                .replaceFirst("__login__", conf.getLogin())
                .replaceFirst("__pass__", conf.getPass());
    }

    public String sendRequest(String function, String functionData) throws IOException {

        String soapRequest = requestTemplate.replaceAll("__function__", function)
                .replaceFirst("__function_data__", functionData);

        try {
            HttpRequest soapAction = HttpRequest.newBuilder(drebedengiUri)
                    .header("SOAPAction", "\"urn:SoapAction\"")
                    .header("Content-Type", SOAP_MEDIA_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofString(soapRequest, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(soapAction, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                return httpResponse.body();
            } else {
                throw new IOException(MessageFormat.format("Response has been failed with code {0}. Body: {1}",
                        httpResponse.statusCode(), httpResponse.body()));
            }
        } catch (InterruptedException e) {
            throw new IOException("Request has been interrupted.", e);
        }
    }
}
