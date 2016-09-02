package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class NewUserRequest extends RequestModel {
    private String mUsername;
    private String mPassword;

    public NewUserRequest(String username, String password) {
        super(Constants.Values.Functions.NEW_USER);
        mUsername = username;
        mPassword = password;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.NewAgent.USERNAME).value(mUsername);
        writer.name(Constants.Fields.NewAgent.PASSWORD).value(mPassword);
    }
}
