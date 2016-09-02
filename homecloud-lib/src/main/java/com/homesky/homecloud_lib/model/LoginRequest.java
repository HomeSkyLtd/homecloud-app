package com.homesky.homecloud_lib.model;

import android.util.JsonWriter;

import java.io.IOException;

public class LoginRequest extends Request {

    String mUsername;
    String mPassword;
    String mToken;

    public LoginRequest(String username, String password, String token) {
        super(Constants.Values.Functions.LOGIN);
        this.mUsername = username;
        this.mPassword = password;
        this.mToken = token;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.Login.USERNAME).value(mUsername);
        writer.name(Constants.Fields.Login.PASSWORD).value(mPassword);
        writer.name(Constants.Fields.Login.TOKEN).value(mToken);
    }
}
