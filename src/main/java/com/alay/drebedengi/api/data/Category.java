package com.alay.drebedengi.api.data;

import com.alay.drebedengi.api.ObjectProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(includeFieldNames = false)
public class Category extends ObjectProperties {

    public Category(Map<String, String> props) {
        super(props);
    }

    @ToString.Include
    public int getId() {
        return getPropertyAsInt("id");
    }

    @ToString.Include
    public String getName() {
        return getProperty("name");
    }

    @ToString.Include
    public boolean getIsHidden() {
        return getPropertyAsBoolean("is_hidden");
    }
}
