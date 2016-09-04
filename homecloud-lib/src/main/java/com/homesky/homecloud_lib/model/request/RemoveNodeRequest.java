package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;

public class RemoveNodeRequest extends RequestModel {
    private String mNodeId, mControllerId;

    public RemoveNodeRequest(String nodeId, String controllerId, int accept) {
        super(Constants.Values.Functions.REMOVE_NODE);
        mNodeId = nodeId;
        mControllerId = controllerId;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.AcceptNode.NODE_ID).value(mNodeId);
        writer.name(Constants.Fields.AcceptNode.CONTROLLER_ID).value(mControllerId);
    }
}
