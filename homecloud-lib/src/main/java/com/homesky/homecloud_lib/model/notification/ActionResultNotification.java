package com.homesky.homecloud_lib.model.notification;

import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

public class ActionResultNotification implements Serializable {
    private static final String TAG = "ActionResultNotif";

    private int mResult;
    private Action mAction;

    public static ActionResultNotification from(String jsonStr) {
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int result = obj.getInt(Constants.Fields.ActionResult.RESULT);
            JSONObject actionJSON = obj.getJSONObject(Constants.Fields.ActionResult.ACTION);
            int nodeId = actionJSON.getInt(Constants.Fields.ActionResult.NODE_ID);
            String controllerId = actionJSON.getString(Constants.Fields.ActionResult.CONTROLLER_ID);
            int commandId = actionJSON.getInt(Constants.Fields.ActionResult.COMMAND_ID);
            BigDecimal value = new BigDecimal(actionJSON.getString(Constants.Fields.ActionResult.VALUE));
            Action action = new Action(nodeId, commandId, value, controllerId);
            return new ActionResultNotification(result, action);
        }
        catch (JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            return null;
        }
    }

    private  ActionResultNotification(int result, Action action) {
        mResult = result;
        mAction = action;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.Fields.ActionResult.NODE_ID + ":" + mAction.getNodeId());
        sb.append("\n");
        sb.append(Constants.Fields.ActionResult.COMMAND_ID + ":" + mAction.getCommandId());
        sb.append("\n");
        sb.append(Constants.Fields.ActionResult.VALUE + ":" + mAction.getValue());
        sb.append("\n");
        sb.append(Constants.Fields.ActionResult.CONTROLLER_ID + ":" + mAction.getControllerId());
        sb.append("\n");
        sb.append(Constants.Fields.ActionResult.RESULT + ":" + mResult);
        return sb.toString();
    }

    public static class Action implements Serializable{
        private int mNodeId, mCommandId;
        private BigDecimal mValue;
        private String mControllerId;

        public Action(int nodeId, int commandId, BigDecimal value, String controllerId) {
            mNodeId = nodeId;
            mCommandId = commandId;
            mValue = value;
            mControllerId = controllerId;
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

        public String getControllerId() {
            return mControllerId;
        }

    }
}
