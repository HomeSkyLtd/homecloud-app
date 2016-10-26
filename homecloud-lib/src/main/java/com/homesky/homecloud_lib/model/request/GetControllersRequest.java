package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class GetControllersRequest extends RequestModel {

    public GetControllersRequest() {
        super(Constants.Values.Functions.GET_CONTROLLERS);
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {

    }
}
