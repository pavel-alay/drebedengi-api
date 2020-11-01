package com.alay.drebedengi.soap.responses.base;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("rawtypes")
public class IntegerResponse<T extends BaseResponse> extends BaseResponse<T> {
    @Getter
    private Integer value;

    @Override
    protected void readTags() {
        this.value = readTagInteger(StringUtils.uncapitalize(getClass().getSimpleName()));
    }
}
