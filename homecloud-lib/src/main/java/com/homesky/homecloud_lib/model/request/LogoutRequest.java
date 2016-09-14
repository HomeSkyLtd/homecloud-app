package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

/**
 * Request representing the logout function, as defined in the HomeCloud protocol.
 */
public class LogoutRequest extends RequestModel {

    private String mToken;

    /**
     * Base constructor.
     * @param token The Firebase Cloud Messaging token associated to the device.
     */
    public LogoutRequest(String token){
        super(Constants.Values.Functions.LOGOUT);
        mToken = token;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.Logout.TOKEN).value(mToken);
    }
}
