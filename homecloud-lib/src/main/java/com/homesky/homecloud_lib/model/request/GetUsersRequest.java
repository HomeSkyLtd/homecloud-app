package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class GetUsersRequest extends RequestModel {

    public GetUsersRequest() {
        super(Constants.Values.Functions.GET_USERS);
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {

    }

}
