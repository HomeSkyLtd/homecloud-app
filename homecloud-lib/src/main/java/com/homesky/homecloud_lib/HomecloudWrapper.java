package com.homesky.homecloud_lib;

import android.util.Log;

import com.homesky.homecloud_lib.model.Rule;
import com.homesky.homecloud_lib.model.response.ConflictingRuleResponse;
import com.homesky.homecloud_lib.model.response.NodesResponse;
import com.homesky.homecloud_lib.model.response.RuleResponse;
import com.homesky.homecloud_lib.model.response.SimpleResponse;
import com.homesky.homecloud_lib.model.response.StateResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class HomecloudWrapper {
    private static final String TAG = "HomecloudWrapper";

    private Homecloud hc;

    interface FunctionCommand {
        SimpleResponse execute();
    }

    public HomecloudWrapper(String url){
        hc = new Homecloud.Builder().url(url).build();
    }

    public void setUrl(String url) {
        hc.setUrl(url);
    }

    public void setUsername(String username) {
        hc.setUsername(username);
    }

    public void setPassword(String password) {
        hc.setPassword(password);
    }

    public void setToken(String token) {
        hc.setToken(token);
    }

    public String getToken() {
        return hc.getToken();
    }


    /**
     * Calls a function and returns a SimpleResponse
     * @param command The {@link FunctionCommand} object containing the function to be called
     * @return The {@link SimpleResponse} object representing the response
     */
    private SimpleResponse callFunctionCommand(FunctionCommand command){
        synchronized (this){
            // First, check if the Homecloud object is properly initialized
            if(!hc.isInitialized()) return null;
            // Then, try executing the command
            SimpleResponse firstTry = command.execute();
            if(firstTry == null) {
                Log.e(TAG, "Failed to send command on first try");
                return null;
            }
            // If unauthorized, try logging in
            if(firstTry.getStatus() == 403){
                SimpleResponse login = hc.login();
                if(login == null || login.getStatus() != 200) {
                    Log.e(TAG, "Failed to log in");
                    return null;
                }
                else return command.execute();
            }
            else return firstTry;
        }
    }

    private SimpleResponse callSR(FunctionCommand command){
        return callFunctionCommand(command);
    }

    private NodesResponse callNR(FunctionCommand command){
        return (NodesResponse)callFunctionCommand(command);
    }

    private StateResponse callStR(FunctionCommand command){
        return (StateResponse) callFunctionCommand(command);
    }

    private RuleResponse callRR(FunctionCommand command){
        return (RuleResponse) callFunctionCommand(command);
    }

    private ConflictingRuleResponse callCRR(FunctionCommand command){
        return (ConflictingRuleResponse) callFunctionCommand(command);
    }

    /**
     * Logs out the user. Subsequent calls to methods may require logging in again
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse logout() {
        synchronized (this) {
            return hc.logout();
        }
    }

    public SimpleResponse newUser(final String username, final String password){
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
                return hc.newUser(username, password);
            }
        });
    }

    public SimpleResponse newAdmin(final String username, final String password){
        return hc.newAdmin(username, password);
    }

    /**
     * Registers an existing controller to the house associated to the agent making the request
     * @param controllerId The id of the controller to be associated
     * @return A {@link SimpleResponse} object representing the response
     */
    public SimpleResponse registerController(final String controllerId){
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
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
    public SimpleResponse newAction(final int nodeId, final String controllerId, final int commandId, final BigDecimal value){
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
                return hc.newAction(nodeId, controllerId, commandId, value);
            }
        });
    }

    /**
     * Create a new automation rule for the house
     * @param rules A list of Rule objects to be added
     * @return A {@link ConflictingRuleResponse} object representing possible conflicting rule.
     */
    public SimpleResponse newRules(final List<Rule> rules){
        return callCRR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
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
    public SimpleResponse setNodeExtra(final Map<String, String> extra, final int nodeId, final String controllerId){
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
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
    public SimpleResponse acceptNode(final int nodeId, final String controllerId, final int accept){
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
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
    public SimpleResponse removeNode(final int nodeId, final String controllerId){
        return callSR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
                return hc.removeNode(nodeId, controllerId);
            }
        });
    }

    /**
     * Get information about nodes registered to the house id of the agent making the request
     * @return A {@link NodesResponse} object containing information of the nodes
     */
    public NodesResponse getNodesInfo() {
        return callNR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
                return hc.getNodesInfo();
            }
        });
    }

    /**
     * Gets the state of the house associated to the logged agent
     * @return A {@link StateResponse} object representing the state of the house
     */
    public StateResponse getHouseState(){
        return callStR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
                return hc.getHouseState();
            }
        });
    }

    /**
     * Get the automation rules for the house associated to the agent making the request
     * @return A {@link RuleResponse} object representing the rules stored for the house
     */
    public RuleResponse getRules(){
        return callRR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
                return hc.getRules();
            }
        });
    }

    /**
     * Get learnt rules from the server
     * @return A {@link RuleResponse} object representing the rules learnt for the house
     */
    public RuleResponse getLearntRules(){
        return callRR(new FunctionCommand() {
            @Override
            public SimpleResponse execute() {
                return hc.getLearntRules();
            }
        });
    }


}
