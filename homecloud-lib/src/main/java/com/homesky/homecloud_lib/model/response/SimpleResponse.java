package com.homesky.homecloud_lib.model.response;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;

public class SimpleResponse {
    private static final String TAG = "SimpleResponse";

    private int mStatus;
    private String mErrorMessage;

    public SimpleResponse(int status, String errorMessage) {
        this.mStatus = status;
        this.mErrorMessage = errorMessage;
    }

    public static SimpleResponse from(String jsonStr){
        try{
            JSONObject obj = new JSONObject(jsonStr);
            int status = obj.getInt(Constants.Fields.Common.STATUS);
            String errorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);
            return new SimpleResponse(status, errorMessage);
        }
        catch(JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            return null;
        }
    }

    public void setFrom(JSONObject obj) throws JSONException {
        mStatus = obj.getInt(Constants.Fields.Common.STATUS);
        mErrorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);
    }

    public void writeJSON(JsonWriter writer) throws IOException{
        writer.name(Constants.Fields.Common.STATUS).value(mStatus);
        writer.name(Constants.Fields.Common.ERROR_MESSAGE).value(mErrorMessage);
    }

    @Override
    public String toString(){
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        try{
            writer.beginObject();
            writeJSON(writer);
            writer.endObject();
        }
        catch (IOException e){
            Log.e(TAG, "Failed writing JSON", e);
        }
        return sw.toString();
    }
}
