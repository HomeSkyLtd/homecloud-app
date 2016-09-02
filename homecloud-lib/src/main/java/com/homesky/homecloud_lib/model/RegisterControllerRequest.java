package com.homesky.homecloud_lib.model;

import android.util.JsonWriter;

import java.io.IOException;

public class RegisterControllerRequest extends RequestModel {
    private String mControllerId;

    public RegisterControllerRequest(String controllerId){
        super(Constants.Values.Functions.REGISTER_CONTROLLER);
        mControllerId = controllerId;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.RegisterController.CONTROLLER_ID).value(mControllerId);
    }
}
