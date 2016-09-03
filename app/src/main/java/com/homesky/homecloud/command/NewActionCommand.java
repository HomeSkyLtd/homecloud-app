package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

public class NewActionCommand implements Command{

    private String mNodeId, mControllerId, mCommandId, mValue;

    public NewActionCommand(String nodeId, String controllerId, String commandId, String value) {
        mNodeId = nodeId;
        mControllerId = controllerId;
        mCommandId = commandId;
        mValue = value;
    }

    @Override
    public SimpleResponse execute() {
        return HomecloudHolder.getInstance().newAction(mNodeId, mControllerId, mCommandId, mValue);
    }
}
