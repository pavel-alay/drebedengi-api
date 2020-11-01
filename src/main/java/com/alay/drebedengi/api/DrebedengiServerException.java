package com.alay.drebedengi.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DrebedengiServerException extends RuntimeException {
    @Getter private final int failCode;
    @Getter private final String failMessage;

    @Override
    public String getMessage() {
        return failMessage + "\n" +
                "Fail code: " + failCode;
    }
}
