package com.alay.drebedengi.soap.functions.base;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.responses.base.BaseResponse;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.alay.drebedengi.soap.SoapConstants.*;

@SuppressWarnings("rawtypes")
public abstract class BaseFunction<R extends BaseResponse> {

    protected StringBuilder getSoapData() {
        return new StringBuilder();
    }

    public abstract R request(WebClient client) throws IOException;

    protected String sendRequest(WebClient client) throws IOException {
        return client.sendRequest(StringUtils.uncapitalize(getClass().getSimpleName()),
                getSoapData().toString());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void append(StringBuilder str, String name, Object value) {
        if (value instanceof Boolean) {
            append(str, name, (Boolean) value);
        } else if (value instanceof Integer) {
            append(str, name, (Integer) value);
        } else if (value instanceof Iterable) {
            append(str, name, (Iterable) value);
        } else if (value instanceof Map) {
            append(str, name, (Map) value);
        } else {
            append(str, name, value.toString());
        }
    }

    protected void append(StringBuilder str, String name, String value) {
        append(str, name, STRING_TYPE, b -> b.append(value));
    }

    protected void append(StringBuilder str, String name, Boolean value) {
        append(str, name, BOOLEAN_TYPE, b -> b.append(value.toString().toLowerCase()));
    }

    protected void append(StringBuilder str, String name, Integer value) {
        append(str, name, INTEGER_TYPE, b -> b.append(value.toString()));
    }

    protected void append(StringBuilder str, String name, Map<String, Object> value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        append(str, name, MAP_TYPE, b -> value.forEach((k, v) -> {
            b.append("<item>");
            append(b, "key", k);
            append(b, "value", v);
            b.append("</item>\n");
        }));
    }

    protected <T> void append(StringBuilder str, String name, @NonNull Iterable<T> iterable) {
        List<T> list = StreamSupport
                .stream(iterable.spliterator(), false)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            return;
        }

        String arrayType = String.format("SOAP-ENC:arrayType=\"%s[%d]\"", getTypeOfArray(list), list.size());

        append(str, name, new String[]{arrayType, "xsi:type=\"SOAP-ENC:Array\""}, s -> {
            s.append("\n");
            list.forEach(item -> append(s, "item", item));
        });
    }

    private <T> String getTypeOfArray(List<T> list) {
        T first = list.get(0);
        String type;
        if (first instanceof Boolean) {
            type = BOOLEAN_TYPE;
        } else if (first instanceof Integer) {
            type = INTEGER_TYPE;
        } else {
            type = STRING_TYPE;
        }

        return type;
    }

    protected void append(StringBuilder str, String name, String type, Consumer<StringBuilder> valueWriter) {
        append(str, name, new String[]{"xsi:type=\"" + type + "\""}, valueWriter);
    }

    protected void append(StringBuilder str, String name, String[] attrs, Consumer<StringBuilder> valueWriter) {
        str.append("<").append(name);
        str.append(Stream.of(attrs).collect(Collectors.joining(" ", " ", "")));
        str.append(">");
        valueWriter.accept(str);
        str.append("</").append(name).append(">\n");
    }
}
