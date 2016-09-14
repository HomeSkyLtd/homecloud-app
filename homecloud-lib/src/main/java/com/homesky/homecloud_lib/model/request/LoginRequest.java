package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

/**
 * Request representing the login function, as defined in the HomeCloud protocol.
 */
public class LoginRequest extends RequestModel {

    String mUsername;
    String mPassword;
    String mToken;

    /**
     * Base constructor.
     * @param username The username.
     * @param password The password.
     * @param token The Firebase Cloud Messaging token associated to the device.
     */
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
