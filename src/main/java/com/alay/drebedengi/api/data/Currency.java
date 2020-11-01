package com.alay.drebedengi.api.data;

import com.alay.drebedengi.api.ObjectProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(includeFieldNames = false, onlyExplicitlyIncluded = true)
public class Currency extends ObjectProperties {

    public Currency(Map<String, String> props) {
        super(props);
    }

    @ToString.Include
    public int getId() {
        return getPropertyAsInt("id");
    }

    @ToString.Include
    public String getCode() {
        return getProperty("code");
    }

    @ToString.Include
    public boolean isHidden() {
        return getPropertyAsBoolean("is_hidden");
    }

    public boolean isNotHidden() {
        return !isHidden();
    }

    public String toHumanReadableString() {
        return String.format("%s - %d", getCode(), getId());
    }
}
