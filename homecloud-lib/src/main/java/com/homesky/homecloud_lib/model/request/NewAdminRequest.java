package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

/**
 * Request representing the newAdmin function, as defined in the HomeCloud protocol.
 */
public class NewAdminRequest extends RequestModel {
    private String mUsername;
    private String mPassword;

    /**
     * Base function.
     * @param username The username associated to the new admin.
     * @param password The password associated to the new admin.
     */
    public NewAdminRequest(String username, String password) {
        super(Constants.Values.Functions.NEW_ADMIN);
        mUsername = username;
        mPassword = password;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.NewAgent.USERNAME).value(mUsername);
        writer.name(Constants.Fields.NewAgent.PASSWORD).value(mPassword);
    }
}
