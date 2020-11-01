package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.BaseFunction;
import com.alay.drebedengi.soap.responses.GetAccessStatusReturn;

import java.io.IOException;

public class GetAccessStatus extends BaseFunction<GetAccessStatusReturn> {
    @Override
    public GetAccessStatusReturn request(WebClient client) throws IOException {
        return new GetAccessStatusReturn().init(sendRequest(client));
    }
}
