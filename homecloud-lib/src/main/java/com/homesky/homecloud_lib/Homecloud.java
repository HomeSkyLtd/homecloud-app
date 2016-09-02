package com.homesky.homecloud_lib;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.LoginRequest;
import com.homesky.homecloud_lib.model.LogoutRequest;
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

    private String mUrl;
    private String mUsername;
    private String mPassword;
    private String mToken;
    private String mCookie = null;

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void setToken(String token) {
        mToken = token;
    }

    /**
     * Logs in with the server using the credentials provided on initialization
     * @return A JSON string following the conventions of the Homecloud protocol
     */
    public String login(){
        RequestModel login = new LoginRequest(mUsername, mPassword, mToken);
        return makeRequest(login);

    }

    /**
     * Logs out the user. Subsequent calls to methods may require logging in again
     * @return A JSON string following the conventions of the Homecloud protocol
     */
    public String logout(){
        RequestModel logout = new LogoutRequest(mToken);
        mCookie = null;
        return makeRequest(logout);
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

        Request.Builder httpRequestBuilder = new Request.Builder()
                .url(mUrl)
                .post(body);
        if(mCookie != null) httpRequestBuilder.addHeader("Cookie", mCookie);
        Request httpRequest = httpRequestBuilder.build();

        if(mCookie != null)
            Log.d(TAG, "Sending " + data + " to " + httpRequest.url() + " with cookie " + mCookie);
        else
            Log.d(TAG, "Sending " + data + " to " + httpRequest.url());

        OkHttpClient client = new OkHttpClient();
        Response response;
        String responseBody = null;
        try {
            response = client.newCall(httpRequest).execute();
            if(response.isSuccessful()) {
                responseBody = response.body().string();
                if(response.header("set-cookie", null) != null) {
                    mCookie = response.header("set-cookie", null);
                    Log.d(TAG, "Setting cookie to " + mCookie);
                }
            }
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

    public static class Builder{
        private String newUrl;
        private String newUsername;
        private String newPassword;
        private String newToken;

        public Builder url(String url){
            newUrl = url;
            return this;
        }

        public Builder username(String username){
            newUsername = username;
            return this;
        }

        public Builder password(String password){
            newPassword = password;
            return this;
        }

        public Builder token(String token){
            newToken = token;
            return this;
        }

        public Homecloud build(){
            Homecloud h = new Homecloud();
            h.setUrl(newUrl);
            h.setUsername(newUsername);
            h.setPassword(newPassword);
            h.setToken(newToken);
            return h;
        }
    }


}
