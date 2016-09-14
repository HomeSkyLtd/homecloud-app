package com.homesky.homecloud_lib.model.response;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the response from the getHouseState function, containing information about house state.
 */
public class StateResponse extends SimpleResponse {
    private static final String TAG = "SimpleResponse";

    List<NodeState> mState;

    protected StateResponse(int status, String errorMessage, List<NodeState> state) {
        super(status, errorMessage);
        mState = state;
    }

    /**
     * Builds a {@link StateResponse} object from a JSON string
     * @param jsonStr The JSON string encoding the house's state
     * @return a {@link StateResponse} object representing the house's state
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
                    int nodeId = stateObj.getInt(Constants.Fields.GetHouseState.NODE_ID);
                    String controllerId = stateObj.getString(Constants.Fields.GetHouseState.CONTROLLER_ID);
                    Map<Integer, BigDecimal> data = new HashMap<>();
                    if(stateObj.has(Constants.Fields.GetHouseState.DATA)) {
                        JSONObject dataJSON = stateObj.getJSONObject(Constants.Fields.GetHouseState.DATA);
                        JSONArray dataKeys = dataJSON.names();
                        for (int j = 0; j < dataKeys.length(); ++j) {
                            Integer key = dataKeys.getInt(j);
                            data.put(key, new BigDecimal(dataJSON.getString(key.toString())));
                        }
                    }

                    Map<Integer, BigDecimal> commands = new HashMap<>();
                    if(stateObj.has(Constants.Fields.GetHouseState.COMMAND)) {
                        JSONObject commandsJSON = stateObj.getJSONObject(Constants.Fields.GetHouseState.COMMAND);
                        JSONArray commandKeys = commandsJSON.names();
                        for (int j = 0; j < commandKeys.length(); ++j) {
                            Integer key = commandKeys.getInt(j);
                            commands.put(key, new BigDecimal(commandsJSON.getString(key.toString())));
                        }
                    }
                    NodeState nodeState = new NodeState(nodeId, controllerId, data, commands);
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
            writer.name(Constants.Fields.GetHouseState.NODE_ID).value(nodeState.getNodeId());
            writer.name(Constants.Fields.GetHouseState.CONTROLLER_ID).value(nodeState.getControllerId());
            if(!nodeState.getData().isEmpty()) {
                writer.name(Constants.Fields.GetHouseState.DATA);
                //begin encode data
                writer.beginObject();
                for (Integer key : nodeState.getData().keySet()) {
                    writer.name(key.toString()).value(nodeState.getData().get(key));
                }
                writer.endObject();
                //end encode data
            }
            if(!nodeState.getCommand().isEmpty()) {
                //begin encode command
                writer.name(Constants.Fields.GetHouseState.COMMAND);
                writer.beginObject();
                for (Integer key : nodeState.getCommand().keySet()) {
                    writer.name(key.toString()).value(nodeState.getCommand().get(key));
                }
                writer.endObject();
                //end encode command
            }
            writer.endObject();
            //end encode state object for (node_id, controller_id)
        }
        writer.endArray();
    }

    /**
     * Represents the state of a node.
     */
    public static class NodeState {
        private int mNodeId;
        private String mControllerId;
        private Map<Integer, BigDecimal> mData, mCommand;

        NodeState(int nodeId, String controllerId, Map<Integer, BigDecimal> data, Map<Integer, BigDecimal> command) {
            mNodeId = nodeId;
            mControllerId = controllerId;
            mData = data;
            mCommand = command;
        }

        /**
         * Gets the node id associated to the state.
         * @return The node id associated to the state.
         */
        public int getNodeId() {
            return mNodeId;
        }

        /**
         * Gets the controller id associated to the node.
         * @return The controller id associated to the node.
         */
        public String getControllerId() {
            return mControllerId;
        }

        /**
         * Gets the values of the data collected by the node in a map format, if it is a sensor. The
         * map maps data ids with their corresponding values.
         * @return A {@link Map} associating data ids and their corresponding values.
         */
        public Map<Integer, BigDecimal> getData() {
            return mData;
        }

        /**
         * Gets the values associated to the commands supported by a node in a map format, if it is a
         * an actuator. The map maps command ids with their corresponding values.
         * @return A {@link Map} associating command ids and their corresponding values.
         */
        public Map<Integer, BigDecimal> getCommand() {
            return mCommand;
        }


        void setControllerId(String controllerId) {
            mControllerId = controllerId;
        }

        void setNodeId(int nodeId) {
            mNodeId = nodeId;
        }

        void setData(Map<Integer, BigDecimal> data) {
            mData = data;
        }

        void setCommand(Map<Integer, BigDecimal> command) {
            mCommand = command;
        }
    }
}
