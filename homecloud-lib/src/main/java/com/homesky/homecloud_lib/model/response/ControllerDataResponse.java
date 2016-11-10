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

    List<Controller> mControllers;

    public ControllerDataResponse(int status, String errorMessage, List<Controller> controllers) {
        super(status, errorMessage);
        mControllers = controllers;
    }

    public static ControllerDataResponse from(String jsonStr){
        if(jsonStr == null) return null;

        try{
            JSONObject obj = new JSONObject(jsonStr);
            int status = obj.getInt(Constants.Fields.Common.STATUS);
            String errorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);

            List<Controller> controllers = new ArrayList<>();
            if(status == 200){
                JSONArray controllersJson = obj.getJSONArray(Constants.Fields.ControllerDataResponse.CONTROLLERS);
                for(int i = 0 ; i < controllersJson.length() ; ++i){
                    JSONObject controllerObjJson = controllersJson.getJSONObject(i);
                    String id = controllerObjJson.getString(Constants.Fields.ControllerDataResponse.ID);
                    String name = controllerObjJson.getString(Constants.Fields.ControllerDataResponse.NAME);
                    controllers.add(new Controller(id, name));
                }
            }
            return new ControllerDataResponse(status, errorMessage, controllers);
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
        for(Controller controller : mControllers){
            writer.beginObject();
            writer.name(Constants.Fields.ControllerDataResponse.ID).value(controller.getId());
            writer.name(Constants.Fields.ControllerDataResponse.NAME).value(controller.getName());
            writer.endObject();
        }
        writer.endArray();
    }

    public List<Controller> getControllers() {
        return mControllers;
    }

    public static class Controller{
        private String mId, mName;

        public Controller(String id, String name) {
            mId = id;
            mName = name;
        }

        public String getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }
    }
}
