package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class GetControllers extends RequestModel {

    public GetControllers() {
        super(Constants.Values.Functions.GET_CONTROLLERS);
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {

    }
}
