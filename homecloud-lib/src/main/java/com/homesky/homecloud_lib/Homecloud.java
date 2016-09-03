package com.homesky.homecloud_lib;

import android.util.Log;

import com.homesky.homecloud_lib.model.request.HouseStateRequest;
import com.homesky.homecloud_lib.model.request.LoginRequest;
import com.homesky.homecloud_lib.model.request.LogoutRequest;
import com.homesky.homecloud_lib.model.request.NewActionRequest;
import com.homesky.homecloud_lib.model.request.NewAdminRequest;
import com.homesky.homecloud_lib.model.request.NewUserRequest;
import com.homesky.homecloud_lib.model.request.RegisterControllerRequest;
import com.homesky.homecloud_lib.model.request.RequestModel;
import com.homesky.homecloud_lib.model.response.SimpleResponse;
import com.homesky.homecloud_lib.model.response.StateResponse;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse login(){
        RequestModel loginReq = new LoginRequest(mUsername, mPassword, mToken);
        String responseStr = makeRequest(loginReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Logs out the user. Subsequent calls to methods may require logging in again
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse logout(){
        RequestModel logoutReq = new LogoutRequest(mToken);
        String responseStr = makeRequest(logoutReq);
        mCookie = null;
        return SimpleResponse.from(responseStr);
    }

    /**
     * Creates a new user associated to the same house as the admin invoking this function
     * @param username The username associated to the new user
     * @param password The password associated to the new user
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse newUser(String username, String password){
        RequestModel newUserReq = new NewUserRequest(username, password);
        String responseStr = makeRequest(newUserReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Creates a new admin associated to a new house.
     * @param username The username associated to the new admin
     * @param password The password associated to the new admin
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse newAdmin(String username, String password){
        RequestModel newAdminReq = new NewAdminRequest(username, password);
        String responseStr = makeRequest(newAdminReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Registers an existing controller to the house associated to the agent making the request
     * @param controllerId The id of the controller to be associated
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse registerController(String controllerId){
        RequestModel registerControllerReq = new RegisterControllerRequest(controllerId);
        String responseStr = makeRequest(registerControllerReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Gets the state of the house associated to the logged agent
     * @return A {@link StateResponse} object representing the state of the house
     */
    public StateResponse getHouseState(){
        RequestModel getHouseStateReq = new HouseStateRequest();
        String responseStr = makeRequest(getHouseStateReq);
        return StateResponse.from(responseStr);
    }

    public SimpleResponse newAction(String nodeId, String controllerId, String commandId, String value){
        RequestModel newActionReq = new NewActionRequest(nodeId, controllerId, commandId, value);
        String responseStr = makeRequest(newActionReq);
        return SimpleResponse.from(responseStr);
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
        String responseBody;
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
