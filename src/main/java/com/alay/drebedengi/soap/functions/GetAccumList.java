package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.IdListFunction;
import com.alay.drebedengi.soap.responses.GetAccumListReturn;

import java.io.IOException;

public class GetAccumList extends IdListFunction<GetAccumList, GetAccumListReturn> {
    @Override
    public GetAccumListReturn request(WebClient client) throws IOException {
        return new GetAccumListReturn().init(sendRequest(client));
    }
}
