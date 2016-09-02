package com.homesky.homecloud;

import android.content.Context;

import com.homesky.homecloud_lib.Homecloud;

public class HomecloudHolder {
    private static Homecloud mInstance = null;

    public static Homecloud getInstance(){
        return mInstance;
    }

    public static void setInstance(String url){
        if (mInstance == null)
            mInstance = new Homecloud(url);
    }
}
