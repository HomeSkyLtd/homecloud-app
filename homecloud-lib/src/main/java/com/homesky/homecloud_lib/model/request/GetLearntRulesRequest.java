package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class GetLearntRulesRequest extends RequestModel{

    public GetLearntRulesRequest(){
        super(Constants.Values.Functions.GET_LEARNT_RULES);
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
    }
}
