package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URLEncoder;

public abstract class RequestModel {
    private static final String TAG = "Request";

    String mFunction;

    public RequestModel(String function){
        mFunction = function;
    }

    abstract void writeContentsJSON(JsonWriter writer) throws IOException;

    private void writeHeaderJSON(JsonWriter writer) throws IOException{
        writer.name(Constants.Fields.Common.FUNCTION).value(mFunction);
    }

    /**
     * Writes the JSON payload to be sent to the server. This method is not suitable for production,
     * since it does not include the POST key "payload=" and does not encode the payload in a proper
     * format to be sent. However, it is useful to visualize the payload contents for debug purpose.
     * @return The string representation of this request
     */
    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        try {
            writer.beginObject();
            writeHeaderJSON(writer);
            writeContentsJSON(writer);
            writer.endObject();
        }
        catch(IOException e){
            Log.e(TAG, "Failed to write string", e);
            return null;
        }
        return sw.toString();
    }

    /**
     * Returns the properly encoded POST body to be sent to the server, representing this request.
     * Uses application/x-www-form-urlencoded encoding.
     * @return A properly formatted string to be used as body in a POST request
     * @throws IOException
     */
    public String getRequest() throws IOException {
        String json = this.toString();
        if(json == null) throw new IOException();
        return Constants.Fields.Common.PAYLOAD + "=" + URLEncoder.encode(json, "UTF-8");
    }
}
