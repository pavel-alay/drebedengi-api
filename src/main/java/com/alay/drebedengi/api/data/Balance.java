package com.alay.drebedengi.api.data;

import com.alay.drebedengi.api.ObjectProperties;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
@ToString(includeFieldNames = false, onlyExplicitlyIncluded = true)
public class Balance extends ObjectProperties {

    public Balance(Map<String, String> props) {
        super(props);
    }

    @ToString.Include
    public BigDecimal getSum() {
        return getPropertyAsMoney("sum");
    }

    @ToString.Include
    public String getCurrencyName() {
        return getProperty("currency_name");
    }

    @ToString.Include
    public String getAccountName() {
        return getProperty("place_name");
    }

    @ToString.Include
    public int getCurrencyId() {
        return getPropertyAsInt("currency_id");
    }

    @ToString.Include
    public int getAccountId() {
        return getPropertyAsInt("place_id");
    }

}
