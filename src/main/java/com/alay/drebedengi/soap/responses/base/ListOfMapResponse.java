package com.alay.drebedengi.soap.responses.base;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ListOfMapResponse<T extends BaseResponse> extends BaseResponse<T> {
    @Getter
    private List<Map<String, String>> list;

    @Override
    protected void readTags() {
        this.list = readTagList(StringUtils.uncapitalize(getClass().getSimpleName()),
                item -> BaseResponse.readTagMap(item,
                        subitem -> BaseResponse.readTagString(subitem, "key"),
                        subitem -> BaseResponse.readTagString(subitem, "value"))
        );
    }
}
