package com.alay.drebedengi.api.data;

import com.alay.drebedengi.api.ObjectProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(includeFieldNames = false, onlyExplicitlyIncluded = true)
public class Account extends ObjectProperties {

    public Account(Map<String, String> props) {
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
    public boolean isHidden() {
        return getPropertyAsBoolean("is_hidden");
    }

    public boolean isDuty() {
        return getPropertyAsBoolean("is_for_duty");
    }

    public boolean isNotDuty() {
        return !isDuty();
    }

    public int getSort() {
        return getPropertyAsInt("sort");
    }

    public String getDescription() {
        return getProperty("description");
    }

    public String getCardNumber() {
        return getDescriptionProperty("Card");
    }

    public String getDefaultIncome() {
        return getDescriptionProperty("Income");
    }

    private String getDescriptionProperty(String property) {
        String keyValue = Arrays.stream(getDescription().split(","))
                .filter(s -> s.contains(property))
                .map(String::trim)
                .findFirst()
                .orElse("");
        String[] split = keyValue.split(":");
        return split.length == 2 ? split[1] : "";
    }

    public boolean isNotHidden() {
        return !isHidden();
    }

    public String toHumanReadableString() {
        if (StringUtils.isEmpty(getDescription())) {
            return String.format("%d: %s", getId(), getName());
        } else {
            return String.format("%d: %s (%s)", getId(), getName(), getDescription());
        }
    }
}
