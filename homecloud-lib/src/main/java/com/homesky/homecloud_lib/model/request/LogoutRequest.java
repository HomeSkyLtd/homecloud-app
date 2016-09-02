package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class LogoutRequest extends RequestModel {

    private String mToken;

    public LogoutRequest(String token){
        super(Constants.Values.Functions.LOGOUT);
        mToken = token;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.Logout.TOKEN).value(mToken);
    }
}
