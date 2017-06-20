package com.homesky.homecloud_lib;

import android.util.Log;

import com.homesky.homecloud_lib.exceptions.NetworkException;
import com.homesky.homecloud_lib.exceptions.NotProperlyInitializedException;
import com.homesky.homecloud_lib.model.Rule;
import com.homesky.homecloud_lib.model.request.LoginRequest;
import com.homesky.homecloud_lib.model.request.RequestModel;
import com.homesky.homecloud_lib.model.response.ConflictingRuleResponse;
import com.homesky.homecloud_lib.model.response.ControllerDataResponse;
import com.homesky.homecloud_lib.model.response.NodesResponse;
import com.homesky.homecloud_lib.model.response.RuleResponse;
import com.homesky.homecloud_lib.model.response.SimpleResponse;
import com.homesky.homecloud_lib.model.response.StateResponse;
import com.homesky.homecloud_lib.model.response.UserDataResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A wrapper for the {@link Homecloud} class that adds method synchronization to allow usage on
 * multi-threaded applications.
 */
public class HomecloudWrapper {
    private static final String TAG = "HomecloudWrapper";

    private Homecloud hc;

    interface FunctionCommand {
        SimpleResponse execute() throws NetworkException;
    }

    public HomecloudWrapper(String url){
        hc = new Homecloud.Builder().url(url).build();
    }

    /**
     * Sets the server url where the requests are to be sent.
     * @param url The server url.
     */
    public void setUrl(String url) {
        hc.setUrl(url);
    }

    /**
     * Sets the username to be used to authenticate the agent making the request. If you're getting
     * a {@link NotProperlyInitializedException}, you probably forgot to call this method.
     * @param username The username used to authenticate the agent.
     */
    public void setUsername(String username) {
        hc.setUsername(username);
    }

    /**
     * Sets the password to be used to authenticate the agent making the request. If you're getting
     * a {@link NotProperlyInitializedException}, you probably forgot to call this method.
     * @param password The username used to authenticate the agent.
     */
    public void setPassword(String password) {
        hc.setPassword(password);
    }

    /**
     * Sets the firebase token to be sent to the server to allow receiving push notifications.
     * @param token The firebase token.
     */
    public void setToken(String token) {
        hc.setToken(token);
    }

    public String getToken() {
        return hc.getToken();
    }

    public boolean isLogged() { return hc.isLogged(); }

    public String getUsername() {
        return hc.getUsername();
    }

    public void invalidateSession() {
        hc.invalidateCookie();
    }


    /**
     * Calls a function and returns a SimpleResponse
     * @param command The {@link FunctionCommand} object containing the function to be called
     * @return The {@link SimpleResponse} object representing the response
     */
    private SimpleResponse callFunctionCommand(FunctionCommand command) throws NetworkException {
        synchronized (this){
            // First, check if the Homecloud object is properly initialized
            if(!hc.isInitialized()) throw new NotProperlyInitializedException();
            // Then, try executing the command
            SimpleResponse firstTry = command.execute();
            if(firstTry == null) {
                Log.e(TAG, "Failed to send command on first try");
                return null;
            }
            // If unauthorized, try logging in
            if(firstTry.getStatus() == 403){
                SimpleResponse login = hc.login();
                if(login == null) {
                    Log.e(TAG, "Failed to log in (received invalid JSON response");
                    return null;
                }
                else if (login.getStatus() != 200){
                    Log.e(TAG, "Failed to log in");
                    return login;
                }
                else return command.execute();
            }
            else return firstTry;
        }
    }

    private SimpleResponse callSR(FunctionCommand command) throws NetworkException {
        return callFunctionCommand(command);
    }

    private NodesResponse callNR(FunctionCommand command) throws NetworkException {
        SimpleResponse sr = callFunctionCommand(command);
        if(sr == null)
            return null;
        else if(sr instanceof NodesResponse)
            return (NodesResponse)sr;
        else{
            return new NodesResponse(sr.getStatus(), sr.getErrorMessage(), new ArrayList<NodesResponse.Node>());
        }
    }

    private StateResponse callStR(FunctionCommand command) throws NetworkException {
        SimpleResponse sr = callFunctionCommand(command);
        if(sr == null)
            return null;
        else if(sr instanceof StateResponse)
            return (StateResponse)sr;
        else
            return new StateResponse(sr.getStatus(), sr.getErrorMessage(), new ArrayList<StateResponse.NodeState>());
    }

    private RuleResponse callRR(FunctionCommand command) throws NetworkException {
        SimpleResponse sr = callFunctionCommand(command);
        if(sr == null)
            return null;
        else if(sr instanceof RuleResponse)
            return (RuleResponse)sr;
        else
            return new RuleResponse(sr.getStatus(), sr.getErrorMessage(), new ArrayList<Rule>());
    }

    private ConflictingRuleResponse callCRR(FunctionCommand command) throws NetworkException {
        SimpleResponse sr = callFunctionCommand(command);
        if(sr == null)
            return null;
        else if(sr instanceof ConflictingRuleResponse)
            return (ConflictingRuleResponse)sr;
        else
            return new ConflictingRuleResponse(sr.getStatus(), sr.getErrorMessage(), null);
    }

    private ControllerDataResponse callCDR(FunctionCommand command) throws NetworkException {
        SimpleResponse sr = callFunctionCommand(command);
        if(sr == null)
            return null;
        else if(sr instanceof ControllerDataResponse)
            return (ControllerDataResponse) sr;
        else
            return new ControllerDataResponse(sr.getStatus(), sr.getErrorMessage(), new ArrayList<ControllerDataResponse.Controller>());
    }

    private UserDataResponse callUDR(FunctionCommand command) throws NetworkException {
        SimpleResponse sr = callFunctionCommand(command);
        if(sr == null)
            return null;
        else if(sr instanceof UserDataResponse)
            return (UserDataResponse) sr;
        else
            return new UserDataResponse(sr.getStatus(), sr.getErrorMessage(), new ArrayList<String>());
    }

    /**
     * Logs in with the server using the credentials provided on initialization.
     * @return A {@link SimpleResponse} object representing the response.
     */
    public SimpleResponse login() throws NetworkException {
        synchronized (this){
            if(!hc.isInitialized()) throw new NotProperlyInitializedException();
            return hc.login();
        }
    }

    /**
     * Logs out the user. Subsequent calls to methods may require logging in again
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse logout() throws NetworkException {
        synchronized (this) {
            return hc.logout();
        }
    }

    public SimpleResponse newUser(final String username, final String password) throws NetworkException {
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.newUser(username, password);
            }
        });
    }

    public SimpleResponse newAdmin(final String username, final String password) throws NetworkException {
        return hc.newAdmin(username, password);
    }

    /**
     * Registers an existing controller to the house associated to the agent making the request
     * @param controllerId The id of the controller to be associated
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse registerController(final String controllerId) throws NetworkException {
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.registerController(controllerId);
            }
        });
    }

    /**
     * Sends an action command to the server
     * @param nodeId The id of the target node
     * @param controllerId The id of the controller associated to the target node
     * @param commandId The id of the command associated to the node
     * @param value The desired value of the command
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse newAction(final int nodeId, final String controllerId, final int commandId, final BigDecimal value)
            throws NetworkException {
        return callSR(new FunctionCommand()  {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.newAction(nodeId, controllerId, commandId, value);
            }
        });
    }

    /**
     * Create a new automation rule for the house
     * @param rules A list of Rule objects to be added
     * @return A {@link ConflictingRuleResponse} object representing possible conflicting rule.
     */
    public ConflictingRuleResponse newRules(final List<Rule> rules) throws NetworkException {
        return callCRR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.newRules(rules);
            }
        });
    }

    /**
     * Set extra information for a node
     * @param extra A map containing extra information for the node
     * @param nodeId The id of the node associated to the provided extra information
     * @param controllerId The id of the controller associated to the node
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse setNodeExtra(final Map<String, String> extra, final int nodeId, final String controllerId)
            throws NetworkException {
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.setNodeExtra(extra, nodeId, controllerId);
            }
        });
    }

    /**
     * Accept or reject the new node on the house of the agent making this request
     * @param nodeId The id of the target node
     * @param controllerId The controller associated to the target node
     * @param accept Whether or not the node will be accepted (1) or rejected (0)
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse acceptNode(final int nodeId, final String controllerId, final int accept)
            throws NetworkException {
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.acceptNode(nodeId, controllerId, accept);
            }
        });
    }

    /**
     * Remove the node from the house associated to the agent makint the request
     * @param nodeId The id of the node to be removed
     * @param controllerId The id of the controller associated to the node to be removed
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse removeNode(final int nodeId, final String controllerId) throws NetworkException {
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.removeNode(nodeId, controllerId);
            }
        });
    }

    /**
     * Get information about nodes registered to the house id of the agent making the request
     * @return A {@link NodesResponse} object containing information of the nodes
     */
    public NodesResponse getNodesInfo() throws NetworkException {
        return callNR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.getNodesInfo();
            }
        });
    }

    /**
     * Gets the state of the house associated to the logged agent
     * @return A {@link StateResponse} object representing the state of the house
     */
    public StateResponse getHouseState() throws NetworkException {
        return callStR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.getHouseState();
            }
        });
    }

    /**
     * Get the automation rules for the house associated to the agent making the request
     * @return A {@link RuleResponse} object representing the rules stored for the house
     */
    public RuleResponse getRules() throws NetworkException {
        return callRR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.getRules();
            }
        });
    }

    /**
     * Get learnt rules from the server
     * @return A {@link RuleResponse} object representing the rules learnt for the house
     */
    public RuleResponse getLearntRules() throws NetworkException {
        return callRR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.getLearntRules();
            }
        });
    }

    /**
     * Removes the specified rule from the database
     * @param rule The rule to be removed from the database
     * @return A {@link SimpleResponse} object representing the response.
     * @throws NetworkException Thrown in case of network connection problems
     */
    public SimpleResponse removeRule(final Rule rule) throws NetworkException{
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.removeRule(rule);
            }
        });
    }

    /**
     * Triggers the rule-learning procedure in the server
     * @return A {@link SimpleResponse} object representing the response.
     * @throws NetworkException Thrown in case of network connection problems
     */
    public SimpleResponse forceRuleLearning() throws NetworkException{
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.forceRuleLearning();
            }
        });
    }

    /**
     * Accepts the rule proposed by the machine learning system.
     * @param nodeId The target node id used in the rule.
     * @param commandId The command id of the target node used in the rule.
     * @param value The value of the command used in the rule.
     * @param controllerId The controller id associated to the target node.
     * @return A {@link ConflictingRuleResponse} object containing a possible conflicting rule.
     */
    public ConflictingRuleResponse acceptRule(final int nodeId, final int commandId, final BigDecimal value,
                                              final String controllerId) throws NetworkException {
        return callCRR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.acceptRule(nodeId, commandId, value, controllerId);
            }
        });
    }

    /**
     * Gets the controllers associated to the current agent.
     * @return A {@link ControllerDataResponse} object containing the data of the associated controllers.
     * @throws NetworkException
     */
    public ControllerDataResponse getControllers() throws NetworkException{
        return callCDR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.getControllers();
            }
        });
    }

    /**
     * Gets the users associated to the same house id as the current admin.
     * @return A {@link UserDataResponse} object containing the data of the associated users.
     * @throws NetworkException
     */
    public UserDataResponse getUsers() throws NetworkException{
        return callUDR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() throws NetworkException {
                return hc.getUsers();
            }
        });
    }


}
