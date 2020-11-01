package com.alay.drebedengi.soap.functions.base;

import com.alay.drebedengi.soap.responses.base.BaseResponse;
import lombok.Builder;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public abstract class ListOfMapFunction<F extends ListOfMapFunction, R extends BaseResponse> extends BaseFunction<R> {
    private final List<Map<String, Object>> list = new ArrayList<>();

    @Builder
    public static class Item {
        @Singular
        private final Map<String, Object> params;
    }

    public static Item.ItemBuilder itemBuilder() {
        return Item.builder();
    }

    @Override
    protected StringBuilder getSoapData() {
        StringBuilder sb = super.getSoapData();
        append(sb, "list", list);
        return sb;
    }

    @SuppressWarnings("unchecked")
    public F add(ListOfMapFunction.Item item) {
        list.add(item.params);
        return (F) this;
    }

    @SuppressWarnings("unchecked")
    public F add(Map<String, Object> item) {
        list.add(item);
        return (F) this;
    }
}
