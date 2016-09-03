package com.homesky.homecloud_lib.model.response;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Base class representing a response from the server.
 */
public class SimpleResponse {
    private static final String TAG = "SimpleResponse";

    private int mStatus;
    private String mErrorMessage;

    protected SimpleResponse(int status, String errorMessage) {
        this.mStatus = status;
        this.mErrorMessage = errorMessage;
    }

    /**
     * Builds a SimpleResponse object from a JSON string
     * @param jsonStr The JSON string encoding the response
     * @return a SimpleResponse object representing the response from the server
     */
    public static SimpleResponse from(String jsonStr){
        if(jsonStr == null) return null;
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

    protected void writeJSON(JsonWriter writer) throws IOException{
        writer.name(Constants.Fields.Common.STATUS).value(mStatus);
        writer.name(Constants.Fields.Common.ERROR_MESSAGE).value(mErrorMessage);
    }

    /**
     * Gets the JSON representation of the response
     * @return The JSON representation of the response
     */
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
