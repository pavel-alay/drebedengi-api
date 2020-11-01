package com.alay.drebedengi.api.operations;

import com.alay.drebedengi.api.ObjectProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * {
 *  operation_type=3,
 *  budget_object_id=40009,
 *  operation_date=2020-02-02 23:56:17,
 *  sum=-5100,
 *  user_nuid=1000000000539,
 *  oper_timestamp=1580676977,
 *  group_id=,
 *  budget_family_id=5,
 *  comment=колбаса,
 *  id=52675,
 *  is_duty=f,
 *  currency_id=17,
 *  place_id=40032
 * }
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(includeFieldNames = false, onlyExplicitlyIncluded = true)
public class GenericRecord extends ObjectProperties {

    public GenericRecord(Map<String, String> props) {
        super(props);
    }

    @ToString.Include
    public int getId() {
        return getPropertyAsInt("id");
    }

    @ToString.Include
    public BigDecimal getSum() {
        return getPropertyAsMoney("sum");
    }

    @ToString.Include
    public int getCategoryId() {
        return getPropertyAsInt("budget_object_id");
    }

    @ToString.Include
    public int getCurrencyId() {
        return getPropertyAsInt("currency_id");
    }

    public GenericRecord setCategoryId(int categoryId) {
        setProperty("budget_object_id", Integer.toString(categoryId));
        return this;
    }

    @ToString.Include
    public int getAccountId() {
        return getPropertyAsInt("place_id");
    }

    @ToString.Include
    public LocalDateTime getDateTime() {
        return getPropertyAsDateTime("operation_date");
    }

    @ToString.Include
    public String getComment() {
        return getProperty("comment");
    }

    public GenericRecord setComment(String value) {
        setProperty("comment", value);
        return this;
    }

    public OperationType getOperationType() {
        return OperationType.parse(getPropertyAsInt("operation_type"));
    }

    public Map<String, Object> convertToMap() {
        HashMap<String, Object> request = new HashMap<>();
        request.put("server_id", getPropertyAsInt("id"));
        for (Map.Entry<String, String> entry : getParams().entrySet()) {
            switch (entry.getKey()) {
                case "id2":
                case "id":
                    continue;
                case "server_change_id":
                case "server_move_id":
                case "place_id":
                case "budget_object_id":
                case "sum":
                case "currency_id":
                case "operation_type":
                    request.put(entry.getKey(), getPropertyAsInt(entry.getKey()));
                    break;
                case "is_duty":
                    request.put(entry.getKey(), getPropertyAsBoolean(entry.getKey()));
                    break;
                default:
                    request.put(entry.getKey(), entry.getValue());
            }
        }
        return request;
    }
}
