package com.homesky.homecloud_lib;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.LoginRequest;

import java.io.IOException;
import java.io.StringWriter;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;


public class Homecloud {
    private static final String TAG = "Homecloud";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType URLENCODED
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private String url;
    private int port = 0;

    public Homecloud(String serverUrl){
        url = serverUrl;
    }

    public String login(String username, String password, String token){

        LoginRequest login = new LoginRequest(username, password, token);
        String data = null;
        try {
            data = login.getRequest();
        }
        catch (IOException e){
            Log.e(TAG, "Error writing JSON", e);
            return null;
        }
        RequestBody body = RequestBody.create(URLENCODED, data);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return makeRequest(request);

    }

    private String makeRequest(Request request){
        try{
            Buffer b = new Buffer();
            request.body().writeTo(b);
            Log.d(TAG, "Sending " + b.readUtf8() + " to " + request.url());
        }
        catch (IOException e) {}

        OkHttpClient client = new OkHttpClient();
        Response response = null;
        String body;
        try {
            response = client.newCall(request).execute();
            if(response.isSuccessful())
                body = response.body().string();
            else {
                Log.e(TAG, "Error: received response " + response.code());
                body = null;
            }
        }
        catch(IOException e){
            Log.e(TAG, "Error making request", e);
            return null;
        }
        return body;
    }


}
