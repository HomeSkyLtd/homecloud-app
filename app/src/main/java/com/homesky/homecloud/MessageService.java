package com.homesky.homecloud;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class MessageService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingSvc";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getData());
        String jsonStr = remoteMessage.getData().get("data");
        try {
            JSONObject msgObj = new JSONObject(jsonStr);
            switch(msgObj.getString(Constants.Fields.Common.NOTIFICATION)){
                case Constants.Values.Notifications.ACTION_RESULT:
                    HomecloudHolder.getInstance().getActionResultSubject().setActionResult(jsonStr);
                    HomecloudHolder.getInstance().getActionResultSubject().notifyObservers();
                    break;
                default:
                    Log.e(TAG, "Invalid notification received");
            }
        }
        catch(JSONException e){
            Log.e(TAG, "Failed to parse JSON", e);
        }
    }
}
