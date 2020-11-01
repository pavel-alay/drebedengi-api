package com.alay.drebedengi.soap.responses.base;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("rawtypes")
public class StringResponse<T extends BaseResponse> extends BaseResponse<T> {
    @Getter
    private String value;

    @Override
    protected void readTags() {
        this.value = readTagString(StringUtils.uncapitalize(getClass().getSimpleName()));
    }
}
