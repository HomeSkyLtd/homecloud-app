package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class GetRulesRequest extends RequestModel{

    public GetRulesRequest(){
        super(Constants.Values.Functions.GET_RULES);
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
    }
}
