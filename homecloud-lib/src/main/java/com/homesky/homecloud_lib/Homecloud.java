package com.homesky.homecloud_lib;

import android.util.Log;

import com.homesky.homecloud_lib.exceptions.NetworkException;
import com.homesky.homecloud_lib.model.Rule;
import com.homesky.homecloud_lib.model.request.AcceptNodeRequest;
import com.homesky.homecloud_lib.model.request.AcceptRuleRequest;
import com.homesky.homecloud_lib.model.request.ForceRuleLearning;
import com.homesky.homecloud_lib.model.request.GetControllersRequest;
import com.homesky.homecloud_lib.model.request.GetLearntRulesRequest;
import com.homesky.homecloud_lib.model.request.GetNodesInfoRequest;
import com.homesky.homecloud_lib.model.request.GetRulesRequest;
import com.homesky.homecloud_lib.model.request.GetUsersRequest;
import com.homesky.homecloud_lib.model.request.HouseStateRequest;
import com.homesky.homecloud_lib.model.request.LoginRequest;
import com.homesky.homecloud_lib.model.request.LogoutRequest;
import com.homesky.homecloud_lib.model.request.NewActionRequest;
import com.homesky.homecloud_lib.model.request.NewAdminRequest;
import com.homesky.homecloud_lib.model.request.NewRulesRequest;
import com.homesky.homecloud_lib.model.request.NewUserRequest;
import com.homesky.homecloud_lib.model.request.RegisterControllerRequest;
import com.homesky.homecloud_lib.model.request.RemoveNodeRequest;
import com.homesky.homecloud_lib.model.request.RemoveRuleRequest;
import com.homesky.homecloud_lib.model.request.RequestModel;
import com.homesky.homecloud_lib.model.request.SetNodeExtraRequest;
import com.homesky.homecloud_lib.model.response.ConflictingRuleResponse;
import com.homesky.homecloud_lib.model.response.ControllerDataResponse;
import com.homesky.homecloud_lib.model.response.NodesResponse;
import com.homesky.homecloud_lib.model.response.RuleResponse;
import com.homesky.homecloud_lib.model.response.SimpleResponse;
import com.homesky.homecloud_lib.model.response.StateResponse;
import com.homesky.homecloud_lib.model.response.UserDataResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class exposes the API for the Homecloud library. Please note that the methods on this class
 * are not thread safe. A synchronized wrapper for this class is implemented in {@link HomecloudWrapper}.
 */
public class Homecloud {
    private static final String TAG = "Homecloud";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType URLENCODED
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private String mUrl = null;
    private String mUsername = null;
    private String mPassword = null;
    private String mToken = null;
    private String mCookie = null;

    String getUrl() {
        return mUrl;
    }

    String getUsername() {
        return mUsername;
    }

    String getPassword() {
        return mPassword;
    }

    String getToken() {
        return mToken;
    }

    String getCookie() {
        return mCookie;
    }

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

    boolean isInitialized(){
        if (mUrl == null || mUsername == null || mPassword == null || mToken == null)
            return false;
        else return true;
    }

    void invalidateCookie(){
        mCookie = null;
    }

    public boolean isLogged() {
        return mCookie != null;
    }

    /**
     * Logs in with the server using the credentials provided on initialization.
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse login() throws NetworkException {
        RequestModel loginReq = new LoginRequest(mUsername, mPassword, mToken);
        String responseStr = makeRequest(loginReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Logs out the user. Subsequent calls to methods may require logging in again.
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse logout() throws NetworkException{
        RequestModel logoutReq = new LogoutRequest(mToken);
        String responseStr = null;
        try{
            responseStr = makeRequest(logoutReq);
        }
        finally {
            mCookie = null;
        }
        return SimpleResponse.from(responseStr);
    }

    /**
     * Creates a new user associated to the same house as the admin invoking this function.
     * @param username The username associated to the new user.
     * @param password The password associated to the new user.
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse newUser(String username, String password) throws NetworkException{
        RequestModel newUserReq = new NewUserRequest(username, password);
        String responseStr = makeRequest(newUserReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Creates a new admin associated to a new house.
     * @param username The username associated to the new admin.
     * @param password The password associated to the new admin.
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse newAdmin(String username, String password) throws NetworkException{
        RequestModel newAdminReq = new NewAdminRequest(username, password);
        String responseStr = makeRequest(newAdminReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Registers an existing controller to the house associated to the agent making the request.
     * @param controllerId The id of the controller to be associated.
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse registerController(String controllerId) throws NetworkException{
        RequestModel registerControllerReq = new RegisterControllerRequest(controllerId);
        String responseStr = makeRequest(registerControllerReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Gets the state of the house associated to the logged agent.
     * @return A {@link StateResponse} object representing the state of the house.
     */
    public StateResponse getHouseState() throws NetworkException{
        RequestModel getHouseStateReq = new HouseStateRequest();
        String responseStr = makeRequest(getHouseStateReq);
        return StateResponse.from(responseStr);
    }

    /**
     * Sends an action command to the server.
     * @param nodeId The id of the target node.
     * @param controllerId The id of the controller associated to the target node.
     * @param commandId The id of the command associated to the node.
     * @param value The desired value of the command.
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse newAction(int nodeId, String controllerId, int commandId, BigDecimal value)
            throws NetworkException{
        RequestModel newActionReq = new NewActionRequest(nodeId, controllerId, commandId, value);
        String responseStr = makeRequest(newActionReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Get the automation rules for the house associated to the agent making the request.
     * @return A {@link RuleResponse} object representing the rules stored for the house.
     */
    public RuleResponse getRules() throws NetworkException{
        RequestModel getRulesReq = new GetRulesRequest();
        String responseStr = makeRequest(getRulesReq);
        return RuleResponse.from(responseStr);
    }

    /**
     * Create a new automation rule for the house.
     * @param rules A list of Rule objects to be added.
     * @return A {@link ConflictingRuleResponse} object representing possible conflicting rule.
     */
    public ConflictingRuleResponse newRules(List<Rule> rules) throws NetworkException{
        RequestModel newRulesReq = new NewRulesRequest(rules);
        String responseStr = makeRequest(newRulesReq);
        return ConflictingRuleResponse.from(responseStr);
    }

    /**
     * Get learnt rules from the server.
     * @return A {@link RuleResponse} object representing the rules learnt for the house.
     */
    public RuleResponse getLearntRules() throws NetworkException{
        RequestModel getLearntRulesReq = new GetLearntRulesRequest();
        String responseStr = makeRequest(getLearntRulesReq);
        return RuleResponse.from(responseStr);
    }

    /**
     * Removes the specified rule from the database
     * @param rule The rule to be removed from the database
     * @return A {@link SimpleResponse} object representing the response.
     * @throws NetworkException Thrown in case of network connection problems
     */
    public SimpleResponse removeRule(Rule rule) throws NetworkException{
        RequestModel removeRuleReq = new RemoveRuleRequest(rule);
        String responseStr = makeRequest(removeRuleReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Triggers the rule-learning procedure in the server
     * @return A {@link SimpleResponse} object representing the response.
     * @throws NetworkException Thrown in case of network connection problems
     */
    public SimpleResponse forceRuleLearning() throws NetworkException{
        RequestModel forceRuleLearningReq = new ForceRuleLearning();
        String responseStr = makeRequest(forceRuleLearningReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Set extra information for a node.
     * @param extra A map containing extra information for the node.
     * @param nodeId The id of the node associated to the provided extra information.
     * @param controllerId The id of the controller associated to the node.
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse setNodeExtra(Map<String, String> extra, int nodeId, String controllerId)
            throws NetworkException{
        RequestModel setNodeExtraReq = new SetNodeExtraRequest(extra, nodeId, controllerId);
        String responseStr = makeRequest(setNodeExtraReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Get information about nodes registered to the house id of the agent making the request.
     * @return A {@link NodesResponse} object containing information of the nodes.
     */
    public NodesResponse getNodesInfo() throws NetworkException{
        RequestModel getNodesInfoReq = new GetNodesInfoRequest();
        String responseStr = makeRequest(getNodesInfoReq);
        return NodesResponse.from(responseStr);
    }

    /**
     * Accept or reject the new node on the house of the agent making this request.
     * @param nodeId The id of the target node.
     * @param controllerId The controller associated to the target node.
     * @param accept Whether or not the node will be accepted (1) or rejected (0).
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse acceptNode(int nodeId, String controllerId, int accept) throws NetworkException{
        RequestModel acceptNodeReq = new AcceptNodeRequest(nodeId, controllerId, accept);
        String responseStr = makeRequest(acceptNodeReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Remove the node from the house associated to the agent makint the request.
     * @param nodeId The id of the node to be removed.
     * @param controllerId The id of the controller associated to the node to be removed.
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse removeNode(int nodeId, String controllerId) throws NetworkException{
        RequestModel removeNodeReq = new RemoveNodeRequest(nodeId, controllerId);
        String responseStr = makeRequest(removeNodeReq);
        return SimpleResponse.from(responseStr);
    }

    /**
     * Accepts the rule proposed by the machine learning system.
     * @param nodeId The target node id used in the rule.
     * @param commandId The command id of the target node used in the rule.
     * @param value The value of the command used in the rule.
     * @param controllerId The controller id associated to the target node.
     * @return A {@link ConflictingRuleResponse} object containing a possible conflicting rule.
     */
    public ConflictingRuleResponse acceptRule(int nodeId, int commandId, BigDecimal value,
                                              String controllerId) throws NetworkException{
        RequestModel removeNodeReq = new AcceptRuleRequest(nodeId, commandId, value, controllerId);
        String responseStr = makeRequest(removeNodeReq);
        return ConflictingRuleResponse.from(responseStr);
    }

    /**
     * Gets the controllers associated to the current agent.
     * @return A {@link ControllerDataResponse} object containing the data of the associated controllers.
     * @throws NetworkException
     */
    public ControllerDataResponse getControllers() throws NetworkException{
        RequestModel getControllersReq = new GetControllersRequest();
        String responseStr = makeRequest(getControllersReq);
        return ControllerDataResponse.from(responseStr);
    }

    /**
     * Gets the users associated to the same house id as the current admin.
     * @return A {@link UserDataResponse} object containing the data of the associated users.
     * @throws NetworkException
     */
    public UserDataResponse getUsers() throws NetworkException{
        RequestModel getUsersReq = new GetUsersRequest();
        String responseStr = makeRequest(getUsersReq);
        return UserDataResponse.from(responseStr);
    }

    private String makeRequest(RequestModel request) throws NetworkException{
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
                Log.d(TAG, "Response: " + responseBody);
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
            throw new NetworkException(e.toString());
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
