package com.alay.drebedengi.soap.functions.base;

import com.alay.drebedengi.soap.responses.base.BaseResponse;

import java.util.LinkedHashMap;

@SuppressWarnings("rawtypes")
public abstract class MapFunction<F extends MapFunction, R extends BaseResponse> extends BaseFunction<R> {
    private final LinkedHashMap<String, Object> params = new LinkedHashMap<>();

    @Override
    protected StringBuilder getSoapData() {
        StringBuilder sb = super.getSoapData();
        append(sb, "params", params);
        return sb;
    }

    @SuppressWarnings("unchecked")
    public F withParam(String name, Object value) {
        params.put(name, value);
        return (F) this;
    }
}
