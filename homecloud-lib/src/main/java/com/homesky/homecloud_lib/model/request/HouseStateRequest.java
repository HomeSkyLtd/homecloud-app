package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class HouseStateRequest extends RequestModel {

    public HouseStateRequest(){
        super(Constants.Values.Functions.GET_HOUSE_STATE);
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
    }
}
