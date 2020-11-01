package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.IdListFunction;
import com.alay.drebedengi.soap.responses.GetPlaceListReturn;

import java.io.IOException;

public class GetPlaceList extends IdListFunction<GetPlaceList, GetPlaceListReturn> {
    @Override
    public GetPlaceListReturn request(WebClient client) throws IOException {
        return new GetPlaceListReturn().init(sendRequest(client));
    }
}
