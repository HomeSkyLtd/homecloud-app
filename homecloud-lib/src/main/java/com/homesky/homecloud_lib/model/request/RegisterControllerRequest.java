package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

/**
 * Request representing the registerController function, as defined in the HomeCloud protocol.
 */
public class RegisterControllerRequest extends RequestModel {
    private String mControllerId;

    /**
     * Base constructor.
     * @param controllerId The id of the controller to be associated.
     */
    public RegisterControllerRequest(String controllerId){
        super(Constants.Values.Functions.REGISTER_CONTROLLER);
        mControllerId = controllerId;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.RegisterController.CONTROLLER_ID).value(mControllerId);
    }
}
