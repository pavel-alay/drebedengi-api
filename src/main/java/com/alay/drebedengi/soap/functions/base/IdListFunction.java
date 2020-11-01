package com.alay.drebedengi.soap.functions.base;

import com.alay.drebedengi.soap.responses.base.BaseResponse;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class IdListFunction<F extends IdListFunction, R extends BaseResponse> extends BaseFunction<R> {
	private final Set<Integer> idList = new LinkedHashSet<>();

    @Override
    protected StringBuilder getSoapData() {
        StringBuilder sb = super.getSoapData();
        append(sb, "idList", idList);
        return sb;
    }

    @SuppressWarnings({"unchecked", "unused"})
	public F withId(Integer id) {
		idList.add(id);
		return (F) this;
	}
}
