package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.BaseFunction;
import com.alay.drebedengi.soap.responses.GetCurrentRevisionReturn;

import java.io.IOException;

public class GetCurrentRevision extends BaseFunction<GetCurrentRevisionReturn> {
    @Override
    public GetCurrentRevisionReturn request(WebClient client) throws IOException {
        return new GetCurrentRevisionReturn().init(sendRequest(client));
    }
}
