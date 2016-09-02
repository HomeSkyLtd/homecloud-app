package com.homesky.homecloud_lib;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.LoginRequest;
import com.homesky.homecloud_lib.model.RequestModel;

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

        RequestModel login = new LoginRequest(username, password, token);

        return makeRequest(login);

    }

    private String makeRequest(RequestModel request){
        String data;
        try {
            data = request.getRequest();
        }
        catch (IOException e){
            Log.e(TAG, "Error writing JSON", e);
            return null;
        }
        RequestBody body = RequestBody.create(URLENCODED, data);
        Request httpRequest = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Log.d(TAG, "Sending " + data + " to " + httpRequest.url());

        OkHttpClient client = new OkHttpClient();
        Response response;
        String responseBody = null;
        try {
            response = client.newCall(httpRequest).execute();
            if(response.isSuccessful())
                responseBody = response.body().string();
            else {
                Log.e(TAG, "Error: received response " + response.code());
                responseBody = null;
            }
        }
        catch(IOException e){
            Log.e(TAG, "Error making request", e);
            return null;
        }
        return responseBody;
    }


}
