package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

/**
 * Request representing the getRules function, as defined in the HomeCloud protocol.
 */
public class GetRulesRequest extends RequestModel{

    /**
     * Base constructor.
     */
    public GetRulesRequest(){
        super(Constants.Values.Functions.GET_RULES);
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
    }
}
