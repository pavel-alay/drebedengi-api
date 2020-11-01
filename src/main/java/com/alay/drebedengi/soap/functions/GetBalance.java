package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.MapFunction;
import com.alay.drebedengi.soap.responses.GetBalanceReturn;

import java.io.IOException;

public class GetBalance extends MapFunction<GetBalance, GetBalanceReturn> {
    @Override
    public GetBalanceReturn request(WebClient client) throws IOException {
        return new GetBalanceReturn().init(sendRequest(client));
    }
}
