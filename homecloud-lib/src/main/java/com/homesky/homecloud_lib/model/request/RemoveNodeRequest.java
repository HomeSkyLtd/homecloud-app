package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

/**
 * Request representing the removeNode function, as defined in the HomeCloud protocol.
 */
public class RemoveNodeRequest extends RequestModel {
    private int mNodeId;
    private String mControllerId;

    /**
     * Base constructor.
     * @param nodeId The id of the node to be removed.
     * @param controllerId The id of the controller associated to the node to be removed.
     */
    public RemoveNodeRequest(int nodeId, String controllerId) {
        super(Constants.Values.Functions.REMOVE_NODE);
        mNodeId = nodeId;
        mControllerId = controllerId;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.RemoveNode.NODE_ID).value(mNodeId);
        writer.name(Constants.Fields.RemoveNode.CONTROLLER_ID).value(mControllerId);
    }
}
