package com.alay.drebedengi.soap.responses.base;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class MapOfMapResponse<T extends BaseResponse> extends BaseResponse<T> {
    @Getter
    private Map<Integer, Map<String, String>> map;

    @Override
    protected void readTags() {
        this.map = readTagMap(StringUtils.uncapitalize(getClass().getSimpleName()),
                item -> BaseResponse.readTagInteger(item, "key"),
                item -> BaseResponse.readTagMap(item, "value",
                        subitem -> BaseResponse.readTagString(subitem, "key"),
                        subitem -> BaseResponse.readTagString(subitem, "value")));
    }
}
