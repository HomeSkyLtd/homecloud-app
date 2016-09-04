package com.homesky.homecloud_lib.model.response;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the response from the getHouseState function
 */
public class StateResponse extends SimpleResponse {
    private static final String TAG = "SimpleResponse";

    List<NodeState> mState;

    protected StateResponse(int status, String errorMessage, List<NodeState> state) {
        super(status, errorMessage);
        mState = state;
    }

    /**
     * Builds a StateResponse object from a JSON string
     * @param jsonStr The JSON string encoding the house's state
     * @return a StateResponse object representing the house's state
     */
    public static StateResponse from(String jsonStr){
        if(jsonStr == null) return null;

        List<NodeState> state = new ArrayList<>();

        try{
            JSONObject obj = new JSONObject(jsonStr);
            int status = obj.getInt(Constants.Fields.Common.STATUS);
            String errorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);

            if(status == 200) {
                JSONArray nodeStates = obj.getJSONArray(Constants.Fields.GetHouseState.STATE);
                for (int i = 0; i < nodeStates.length(); ++i) {
                    JSONObject stateObj = nodeStates.getJSONObject(i);
                    NodeState nodeState = new NodeState();
                    nodeState.nodeId = stateObj.getString(Constants.Fields.GetHouseState.NODE_ID);
                    nodeState.controllerId = stateObj.getString(Constants.Fields.GetHouseState.CONTROLLER_ID);
                    JSONObject data = stateObj.getJSONObject(Constants.Fields.GetHouseState.DATA);
                    JSONArray dataKeys = data.names();
                    for (int j = 0; j < dataKeys.length(); ++j) {
                        String key = dataKeys.getString(j);
                        nodeState.data.put(key, data.getString(key));
                    }
                    JSONObject commands = stateObj.getJSONObject(Constants.Fields.GetHouseState.COMMAND);
                    JSONArray commandKeys = commands.names();
                    for (int j = 0; j < commandKeys.length(); ++j) {
                        String key = commandKeys.getString(j);
                        nodeState.command.put(key, commands.getString(key));
                    }
                    state.add(nodeState);
                }
            }

            return new StateResponse(status, errorMessage, state);
        }
        catch(JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            return null;
        }
    }

    @Override
    protected void writeJSON(JsonWriter writer) throws IOException {
        super.writeJSON(writer);
        writer.name(Constants.Fields.GetHouseState.STATE);
        writer.beginArray();
        for(NodeState nodeState : mState){
            //begin encode state object for (node_id, controller_id)
            writer.beginObject();
            writer.name(Constants.Fields.GetHouseState.NODE_ID).value(nodeState.nodeId);
            writer.name(Constants.Fields.GetHouseState.CONTROLLER_ID).value(nodeState.controllerId);
            writer.name(Constants.Fields.GetHouseState.DATA);
            //encode data
            writer.beginObject();
            for(String key : nodeState.data.keySet()){
                writer.name(key).value(nodeState.data.get(key));
            }
            writer.endObject();
            //end encode data
            //begin encode command
            writer.name(Constants.Fields.GetHouseState.COMMAND);
            writer.beginObject();
            for(String key : nodeState.command.keySet()){
                writer.name(key).value(nodeState.command.get(key));
            }
            writer.endObject();
            //end encode command
            writer.endObject();
            //end encode state object for (node_id, controller_id)
        }
        writer.endArray();
    }

    public static class NodeState {
        String nodeId;
        String controllerId;
        Map<String, String> data = new HashMap<>();
        Map<String, String> command = new HashMap<>();
    }
}
