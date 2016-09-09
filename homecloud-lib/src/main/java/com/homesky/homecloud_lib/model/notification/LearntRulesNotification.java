package com.homesky.homecloud_lib.model.notification;

import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class LearntRulesNotification implements Serializable, Notification {
    private static final String TAG = "LearntRulesNotif";

    private int mNumberOfRules;

    public LearntRulesNotification(int numberOfRules) {
        mNumberOfRules = numberOfRules;
    }

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

    @Override
    public String toString() {
        return Constants.Fields.LearntRules.QUANTITY + ": " + mNumberOfRules;
    }
}
