package com.homesky.homecloud_lib.model.notification;

import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NewCommandNotification implements Serializable, Notification {
    private static final String TAG = "NewCommandNotif";

    List<NodeCommand> mCommand;

    public NewCommandNotification(List<NodeCommand> data){
        mCommand = data;
    }

    public static NewCommandNotification from(String jsonStr) {
        List<NodeCommand> command = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            JSONArray dataJson = obj.getJSONArray(Constants.Fields.NewCommandNotification.COMMAND);
            for(int i = 0 ; i < dataJson.length() ; ++i){
                JSONObject dataJsonObj = dataJson.getJSONObject(i);
                int nodeId = dataJsonObj.getInt(Constants.Fields.NewCommandNotification.NODE_ID);
                int dataId = dataJsonObj.getInt(Constants.Fields.NewCommandNotification.COMMAND_ID);
                BigDecimal value = new BigDecimal(dataJsonObj.getString(Constants.Fields.NewCommandNotification.VALUE));
                command.add(new NodeCommand(nodeId, dataId, value));
            }
            return new NewCommandNotification(command);
        }
        catch(JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            return null;
        }
    }

    public static class NodeCommand{
        private int mNodeId, mCommandId;
        private BigDecimal mValue;

        public NodeCommand(int nodeId, int commandId, BigDecimal value) {
            this.mNodeId = nodeId;
            this.mCommandId = commandId;
            this.mValue = value;
        }

        public int getNodeId() {
            return mNodeId;
        }

        public int getCommandId() {
            return mCommandId;
        }

        public BigDecimal getValue() {
            return mValue;
        }
    }
}
