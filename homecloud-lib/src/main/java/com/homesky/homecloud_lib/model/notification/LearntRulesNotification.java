package com.homesky.homecloud_lib.model.notification;

import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Represents the contents of the Notify Learnt Rules notification, as defined in the Homecloud protocol.
 */
public class LearntRulesNotification implements Serializable, Notification {
    private static final String TAG = "LearntRulesNotif";

    private int mNumberOfRules;

    /**
     * Builds an {@link LearntRulesNotification} object representing the JSON payload received as part of the
     * Notify Learnt Rules notification.
     * @param jsonStr The JSON payload received as part of the notification, as a String.
     * @return The {@link LearntRulesNotification} object representing the payload.
     */
    public static LearntRulesNotification from(String jsonStr){
        try{
            JSONObject obj = new JSONObject(jsonStr);
            int numberOfRules = obj.getInt(Constants.Fields.LearntRules.QUANTITY);
            return new LearntRulesNotification(numberOfRules);
        }
        catch(JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            return null;
        }

    }

    public LearntRulesNotification(int numberOfRules) {
        mNumberOfRules = numberOfRules;
    }

    /**
     * Gets the number of learnt rules.
     * @return The number of learnt rules.
     */
    public int getNumberOfRules() {
        return mNumberOfRules;
    }

    @Override
    public String toString() {
        return Constants.Fields.LearntRules.QUANTITY + ": " + mNumberOfRules;
    }
}
