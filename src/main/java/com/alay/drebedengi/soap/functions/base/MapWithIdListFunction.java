package com.alay.drebedengi.soap.functions.base;

import com.alay.drebedengi.soap.responses.base.BaseResponse;

import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings("rawtypes")
public abstract class MapWithIdListFunction<F extends MapWithIdListFunction, R extends BaseResponse> extends MapFunction<F, R> {
    private final Set<Integer> idList = new LinkedHashSet<>();

    protected StringBuilder getSoapData() {
        StringBuilder str = super.getSoapData();
        append(str, "idList", idList);
        return str;
    }

    @SuppressWarnings("unchecked")
    public F withId(Integer id) {
        idList.add(id);
        return (F) this;
    }
}
