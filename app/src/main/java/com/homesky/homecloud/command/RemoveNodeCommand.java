package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

public class RemoveNodeCommand implements Command{
    int mNodeId;
    String mControllerId;

    public RemoveNodeCommand(int nodeId, String controllerId) {
        mNodeId = nodeId;
        mControllerId = controllerId;
    }

    @Override
    public SimpleResponse execute() {
        return HomecloudHolder.getInstance().removeNode(mNodeId, mControllerId);
    }
}
