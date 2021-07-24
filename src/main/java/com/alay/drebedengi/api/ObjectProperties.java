package com.alay.drebedengi.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ObjectProperties {
    @Getter(AccessLevel.PROTECTED)
    private Map<String, String> params;

    protected String getProperty(String property) {
        return params.get(property);
    }

    protected void setProperty(String property, String value) {
        params.put(property, value);
    }

    protected boolean getPropertyAsBoolean(String property) {
        String value = getProperty(property);
        switch (value) {
            case "false":
            case "f":
                return false;
            case "true":
            case "t":
                return true;
            default:
                throw new IllegalArgumentException("Can't parse as boolean: " + value);
        }
    }

    public LocalDateTime getPropertyAsDateTime(String property) {
        return DateTimeUtils.stringToDateTime(getProperty(property));
    }

    public int getPropertyAsInt(String property) {
        return Integer.parseInt(getProperty(property));
    }

    protected long getPropertyAsLong(String property) {
        return Long.parseLong(getProperty(property));
    }

    public BigDecimal getPropertyAsMoney(String property) {
        return BigDecimal.valueOf(getPropertyAsLong(property)).divide(BigDecimal.valueOf(100));
    }
}
