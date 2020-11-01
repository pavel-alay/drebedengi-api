package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.IdListFunction;
import com.alay.drebedengi.soap.responses.GetSourceListReturn;

import java.io.IOException;

public class GetSourceList extends IdListFunction<GetSourceList, GetSourceListReturn> {
    @Override
    public GetSourceListReturn request(WebClient client) throws IOException {
        return new GetSourceListReturn().init(sendRequest(client));
    }
}
