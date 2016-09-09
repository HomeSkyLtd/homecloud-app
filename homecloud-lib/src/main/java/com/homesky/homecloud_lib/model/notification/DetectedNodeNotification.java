package com.homesky.homecloud_lib.model.notification;

import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DetectedNodeNotification implements Notification, Serializable {
    private static final String TAG = "DetectedNodeNotif";
    private int mNumberOfNodes;

    public DetectedNodeNotification(int numberOfNodes) {
        mNumberOfNodes = numberOfNodes;
    }

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

    public int getNumberOfNodes() {
        return mNumberOfNodes;
    }

    @Override
    public String toString() {
        return Constants.Fields.DetectedNode.QUANTITY + ": " + mNumberOfNodes;
    }
}
