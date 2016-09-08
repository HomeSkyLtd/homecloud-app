package com.homesky.homecloud;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.notification.ActionResultNotification;

import org.json.JSONException;
import org.json.JSONObject;


public class MessageService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingSvc";
    public static final String NOTIF_RESULT = "com.homesky.homecloud_lib.NOTIF_RESULT";
    public static final String NOTIF_MESSAGE = "com.homesky.homecloud_lib.NOTIF_MESSAGE";

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getData());
        String jsonStr = remoteMessage.getData().get("data");
        try {
            JSONObject msgObj = new JSONObject(jsonStr);
            switch(msgObj.getString(Constants.Fields.Common.NOTIFICATION)){
                case Constants.Values.Notifications.ACTION_RESULT:
                    Intent i = new Intent(NOTIF_RESULT);
                    ActionResultNotification notification = ActionResultNotification.from(jsonStr);
                    if(notification == null)
                        Log.e(TAG, "Action result in invalid format");
                    else{
                        i.putExtra(NOTIF_MESSAGE, notification);
                        broadcaster.sendBroadcast(i);
                    }
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
