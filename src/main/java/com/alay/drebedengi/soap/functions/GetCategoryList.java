package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.IdListFunction;
import com.alay.drebedengi.soap.responses.GetCategoryListReturn;

import java.io.IOException;

public class GetCategoryList extends IdListFunction<GetCategoryList, GetCategoryListReturn> {
    @Override
    public GetCategoryListReturn request(WebClient client) throws IOException {
        return new GetCategoryListReturn().init(sendRequest(client));
    }
}
