package com.homesky.homecloud_lib.model.response;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerDataResponse extends SimpleResponse {
    private static final String TAG = "ControllerDataResp";

    List<String> mControllerIds;

    public ControllerDataResponse(int status, String errorMessage, List<String> controllerIds) {
        super(status, errorMessage);
        mControllerIds = controllerIds;
    }

    public static ControllerDataResponse from(String jsonStr){
        if(jsonStr == null) return null;

        try{
            JSONObject obj = new JSONObject(jsonStr);
            int status = obj.getInt(Constants.Fields.Common.STATUS);
            String errorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);

            List<String> controllerIds = new ArrayList<>();
            if(status == 200){
                JSONArray ids = obj.getJSONArray(Constants.Fields.GetControllers.CONTROLLERS);
                for(int i = 0 ; i < ids.length() ; ++i){
                    controllerIds.add(ids.getString(i));
                }
            }
            return new ControllerDataResponse(status, errorMessage, controllerIds);
        }
        catch(JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void writeJSON(JsonWriter writer) throws IOException {
        super.writeJSON(writer);
        writer.name(Constants.Fields.GetControllers.CONTROLLERS);
        writer.beginArray();
        for(String id : mControllerIds){
            writer.value(id);
        }
        writer.endArray();
    }
}
