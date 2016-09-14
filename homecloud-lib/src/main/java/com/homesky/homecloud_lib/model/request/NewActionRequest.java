package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Request representing the newAction function, as defined in the HomeCloud protocol.
 */
public class NewActionRequest extends RequestModel {

    private int mNodeId, mCommandId;
    private String mControllerId;
    private BigDecimal mValue;

    /**
     * Base constructor.
     * @param nodeId The id of the target node.
     * @param controllerId The id of the controller associated to the target node.
     * @param commandId The id of the command associated to the node.
     * @param value The desired value of the command.
     */
    public NewActionRequest(int nodeId, String controllerId, int commandId, BigDecimal value) {
        super(Constants.Values.Functions.NEW_ACTION);
        mNodeId = nodeId;
        mControllerId = controllerId;
        mCommandId = commandId;
        mValue = value;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.NewAction.ACTION);
        writer.beginObject();
        writer.name(Constants.Fields.NewAction.NODE_ID).value(mNodeId);
        writer.name(Constants.Fields.NewAction.CONTROLLER_ID).value(mControllerId);
        writer.name(Constants.Fields.NewAction.COMMAND_ID).value(mCommandId);
        writer.name(Constants.Fields.NewAction.VALUE).value(mValue);
        writer.endObject();
    }
}
