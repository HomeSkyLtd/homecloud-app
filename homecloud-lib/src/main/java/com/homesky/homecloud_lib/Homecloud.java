package com.homesky.homecloud_lib;

import android.util.Log;

import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.request.AcceptNodeRequest;
import com.homesky.homecloud_lib.model.request.GetLearntRulesRequest;
import com.homesky.homecloud_lib.model.request.GetNodesInfoRequest;
import com.homesky.homecloud_lib.model.request.GetRulesRequest;
import com.homesky.homecloud_lib.model.request.HouseStateRequest;
import com.homesky.homecloud_lib.model.request.LoginRequest;
import com.homesky.homecloud_lib.model.request.LogoutRequest;
import com.homesky.homecloud_lib.model.request.NewActionRequest;
import com.homesky.homecloud_lib.model.request.NewAdminRequest;
import com.homesky.homecloud_lib.model.request.NewRulesRequest;
import com.homesky.homecloud_lib.model.request.NewUserRequest;
import com.homesky.homecloud_lib.model.request.RegisterControllerRequest;
import com.homesky.homecloud_lib.model.request.RemoveNodeRequest;
import com.homesky.homecloud_lib.model.request.RequestModel;
import com.homesky.homecloud_lib.model.request.SetNodeExtraRequest;
import com.homesky.homecloud_lib.model.response.NodesResponse;
import com.homesky.homecloud_lib.model.response.RuleResponse;
import com.homesky.homecloud_lib.model.response.SimpleResponse;
import com.homesky.homecloud_lib.model.response.StateResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    /**
     * Sends an action command to the server
     * @param nodeId The id of the target node
     * @param controllerId The id of the controller associated to the target node
     * @param commandId The id of the command associated to the node
     * @param value The desired value of the command
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse newAction(int nodeId, int controllerId, int commandId, BigDecimal value){
        RequestModel newActionReq = new NewActionRequest(nodeId, controllerId, commandId, value);
        String responseStr = makeRequest(newActionReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Get the automation rules for the house associated to the agent making the request
     * @return A {@link RuleResponse} object representing the rules stored for the house
     */
    public RuleResponse getRules(){
        RequestModel getRulesReq = new GetRulesRequest();
        String responseStr = makeRequest(getRulesReq);
        return RuleResponse.from(responseStr);
    }

    /**
     * Create a new automation rule for the house
     * @param nodeId The id of the target node whose state will be changed by the rule
     * @param controllerId The id of the controller associated to the target node
     * @param commandId The id of the command to be triggered by the rule
     * @param value The desired value of the command after the rule is activated
     * @param clause The condition associated to the rule, in CNF form
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse newRules(int nodeId, int controllerId, int commandId,
                                   BigDecimal value, List<List<Proposition>> clause){
        RequestModel newRulesReq = new NewRulesRequest(nodeId, controllerId, commandId, value, clause);
        String responseStr = makeRequest(newRulesReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Get learnt rules from the server
     * @return A {@link RuleResponse} object representing the rules learnt for the house
     */
    public RuleResponse getLearntRules(){
        RequestModel getLearntRulesReq = new GetLearntRulesRequest();
        String responseStr = makeRequest(getLearntRulesReq);
        return RuleResponse.from(responseStr);
    }

    /**
     * Set extra information for a node
     * @param extra A map containing extra information for the node
     * @param nodeId The id of the node associated to the provided extra information
     * @param controllerId The id of the controller associated to the node
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse setNodeExtra(Map<String, String> extra, String nodeId, String controllerId){
        RequestModel setNodeExtraReq = new SetNodeExtraRequest(extra, nodeId, controllerId);
        String responseStr = makeRequest(setNodeExtraReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Get information about nodes registered to the house id of the agent making the request
     * @return A {@link NodesResponse} object containing information of the nodes
     */
    public NodesResponse getNodesInfo(){
        RequestModel getNodesInfoReq = new GetNodesInfoRequest();
        String responseStr = makeRequest(getNodesInfoReq);
        return NodesResponse.from(responseStr);
    }

    /**
     * Accept or reject the new node on the house of the agent making this request
     * @param nodeId The id of the target node
     * @param controllerId The controller associated to the target node
     * @param accept Whether or not the node will be accepted (1) or rejected (0)
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse acceptNode(String nodeId, String controllerId, int accept){
        RequestModel acceptNodeReq = new AcceptNodeRequest(nodeId, controllerId, accept);
        String responseStr = makeRequest(acceptNodeReq);
        return NodesResponse.from(responseStr);
    }

    /**
     * Remove the node from the house associated to the agent makint the request
     * @param nodeId The id of the node to be removed
     * @param controllerId The id of the controller associated to the node to be removed
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse removeNode(String nodeId, String controllerId){
        RequestModel removeNodeReq = new RemoveNodeRequest(nodeId, controllerId);
        String responseStr = makeRequest(removeNodeReq);
        return NodesResponse.from(responseStr);
    }

    private String makeRequest(RequestModel request){
        // Print log message
        if(mCookie != null)
            Log.d(TAG, "Sending " + request.toString() + " to " + mUrl + " with cookie " + mCookie);
        else
            Log.d(TAG, "Sending " + request.toString() + " to " + mUrl);

        // Get request body as string, and encode it properly
        String data;
        try {
            data = request.getRequest();
        }
        catch (IOException e){
            Log.e(TAG, "Error writing JSON", e);
            return null;
        }
        RequestBody body = RequestBody.create(URLENCODED, data);

        // Build the request object, specifying the method and destination URL
        Request.Builder httpRequestBuilder = new Request.Builder()
                .url(mUrl)
                .post(body);
        // Set the cookie, if available
        if(mCookie != null) httpRequestBuilder.addHeader("Cookie", mCookie);
        Request httpRequest = httpRequestBuilder.build();

        // Make the request
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
