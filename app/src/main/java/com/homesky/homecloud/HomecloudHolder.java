package com.homesky.homecloud;

import android.content.Context;
import android.util.Log;

import com.homesky.homecloud_lib.Homecloud;
import com.homesky.homecloud_lib.HomecloudWrapper;

public class HomecloudHolder {
    private static final String TAG = "HomecloudHolder";

    private static HomecloudWrapper instance = null;

    private static String url = null;
    private static String username = null;
    private static String password = null;
    private static String token = null;

    public static void setUrl(String url) {
        HomecloudHolder.url = url;
        if(instance != null)
            instance.setUrl(url);
    }

    public static void setUsername(String username) {
        HomecloudHolder.username = username;
        if(instance != null)
            instance.setUsername(username);
    }

    public static void setPassword(String password) {
        HomecloudHolder.password = password;
        if(instance != null)
            instance.setPassword(password);
    }

    public static void setToken(String token) {
        HomecloudHolder.token = token;
        if(instance != null)
            instance.setToken(token);
    }

    public static synchronized HomecloudWrapper getInstance(){
        if(url == null){
            Log.e(TAG, "Trying to instantiate Homecloud without url");
            return null;
        }
        if(instance == null) {
//            instance = new Homecloud.Builder()
//                    .url(url)
//                    .username(username)
//                    .password(password)
//                    .token(token)
//                    .build();
            instance = new HomecloudWrapper(url);
            instance.setUsername(username);
            instance.setPassword(password);
            instance.setToken(token);
        }
        return instance;
    }

}
