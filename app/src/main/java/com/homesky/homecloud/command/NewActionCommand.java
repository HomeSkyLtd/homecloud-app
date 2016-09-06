package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

import java.math.BigDecimal;

public class NewActionCommand implements Command{

    private int mNodeId, mControllerId, mCommandId;
    private BigDecimal mValue;

    public NewActionCommand(int nodeId, int controllerId, int commandId, BigDecimal value) {
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
