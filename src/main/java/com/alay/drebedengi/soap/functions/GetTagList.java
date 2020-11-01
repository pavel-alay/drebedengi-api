package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.IdListFunction;
import com.alay.drebedengi.soap.responses.GetTagListReturn;

import java.io.IOException;

public class GetTagList extends IdListFunction<GetTagList, GetTagListReturn> {
    @Override
    public GetTagListReturn request(WebClient client) throws IOException {
        return new GetTagListReturn().init(sendRequest(client));
    }
}
