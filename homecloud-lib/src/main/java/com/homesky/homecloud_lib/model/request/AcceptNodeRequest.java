package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

/**
 * Request representing the acceptNode function, as defined in the HomeCloud protocol.
 */
public class AcceptNodeRequest extends RequestModel{
    private int mNodeId;
    private String mControllerId;
    int mAccept;

    /**
     * Base constructor.
     * @param nodeId The id of the target node.
     * @param controllerId The controller associated to the target node.
     * @param accept Whether or not the node will be accepted (1) or rejected (0).
     */
    public AcceptNodeRequest(int nodeId, String controllerId, int accept) {
        super(Constants.Values.Functions.ACCEPT_NODE);
        mNodeId = nodeId;
        mControllerId = controllerId;
        mAccept = accept;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.AcceptNode.ACCEPT).value(mAccept);
        writer.name(Constants.Fields.AcceptNode.NODE_ID).value(mNodeId);
        writer.name(Constants.Fields.AcceptNode.CONTROLLER_ID).value(mControllerId);
    }
}
