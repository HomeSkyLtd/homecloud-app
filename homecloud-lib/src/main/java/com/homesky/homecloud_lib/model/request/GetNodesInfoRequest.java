package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class GetNodesInfoRequest extends RequestModel{

    public GetNodesInfoRequest(){
        super(Constants.Values.Functions.GET_NODES_INFO);
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
    }
}
