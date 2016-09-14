package com.homesky.homecloud_lib.model.notification;

import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents the contents of the Notify Action Result notification, as defined in the Homecloud protocol.
 */
public class ActionResultNotification implements Serializable, Notification {
    private static final String TAG = "ActionResultNotif";

    private int mResult;
    private Action mAction;

    /**
     * Builds an {@link ActionResultNotification} object representing the JSON payload received as part of the
     * Notify Action Result notification.
     * @param jsonStr The JSON payload received as part of the notification, as a String.
     * @return The {@link ActionResultNotification} object representing the payload.
     */
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

    /**
     * Gets the result code of the New Action request.
     * @return 1 if successful, 0 otherwise.
     */
    public int getResult() {
        return mResult;
    }

    /**
     * Gets the action related to the notification.
     * @return An {@link Action} object representing the action.
     */
    public Action getAction() {
        return mAction;
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

    /**
     * Represents an action that a user can take in the house's actuators.
     */
    public static class Action implements Serializable{
        private int mNodeId, mCommandId;
        private BigDecimal mValue;
        private String mControllerId;

        Action(int nodeId, int commandId, BigDecimal value, String controllerId) {
            mNodeId = nodeId;
            mCommandId = commandId;
            mValue = value;
            mControllerId = controllerId;
        }

        /**
         * Gets the node id of the target actuator.
         * @return The node id of the target actuator.
         */
        public int getNodeId() {
            return mNodeId;
        }

        /**
         * Gets the command id associated to the action.
         * @return The command id associated to the action.
         */
        public int getCommandId() {
            return mCommandId;
        }

        /**
         * Gets the new value of the command associated to the action.
         * @return The new value of the command.
         */
        public BigDecimal getValue() {
            return mValue;
        }

        /**
         * Gets the controller id associated to the target actuator in the action.
         * @return The controller id associated to the target actuator.
         */
        public String getControllerId() {
            return mControllerId;
        }

    }
}
