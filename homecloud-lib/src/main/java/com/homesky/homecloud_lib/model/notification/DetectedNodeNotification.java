package com.homesky.homecloud_lib.model.notification;

import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Represents the contents of the Notify Detected Node notification, as defined in the Homecloud protocol.
 */
public class DetectedNodeNotification implements Notification, Serializable {
    private static final String TAG = "DetectedNodeNotif";
    private int mNumberOfNodes;

    /**
     * Builds an {@link DetectedNodeNotification} object representing the JSON payload received as part of the
     * Notify Detected Node notification.
     * @param jsonStr The JSON payload received as part of the notification, as a String.
     * @return The {@link DetectedNodeNotification} object representing the payload.
     */
    public static DetectedNodeNotification from(String jsonStr){
        try{
            JSONObject obj = new JSONObject(jsonStr);
            int numberOfNodes = obj.getInt(Constants.Fields.DetectedNode.QUANTITY);
            return new DetectedNodeNotification(numberOfNodes);
        }
        catch(JSONException e){
            Log.e(TAG, "Failed parsing JSON");
            return null;
        }
    }

    private DetectedNodeNotification(int numberOfNodes) {
        mNumberOfNodes = numberOfNodes;
    }

    /**
     * Gets the number of detected nodes.
     * @return The number of detected nodes.
     */
    public int getNumberOfNodes() {
        return mNumberOfNodes;
    }

    @Override
    public String toString() {
        return Constants.Fields.DetectedNode.QUANTITY + ": " + mNumberOfNodes;
    }
}
