package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.BaseFunction;
import com.alay.drebedengi.soap.responses.GetUserIdByLoginReturn;

import java.io.IOException;

public class GetUserIdByLogin extends BaseFunction<GetUserIdByLoginReturn> {
    @Override
    public GetUserIdByLoginReturn request(WebClient client) throws IOException {
        return new GetUserIdByLoginReturn().init(sendRequest(client));
    }
}
