package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.IdListFunction;
import com.alay.drebedengi.soap.responses.GetCurrencyListReturn;

import java.io.IOException;

public class GetCurrencyList extends IdListFunction<GetCurrencyList, GetCurrencyListReturn> {
    @Override
    public GetCurrencyListReturn request(WebClient client) throws IOException {
        return new GetCurrencyListReturn().init(sendRequest(client));
    }
}
