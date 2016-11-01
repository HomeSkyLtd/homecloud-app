package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;
import java.math.BigDecimal;

public class AcceptRuleRequest extends RequestModel{
    private int mNodeId, mCommandId;
    private BigDecimal mValue;
    private String mControllerId;

    public AcceptRuleRequest(int nodeId, int commandId, BigDecimal value, String controllerId) {
        super(Constants.Values.Functions.ACCEPT_RULE);
        mNodeId = nodeId;
        mCommandId = commandId;
        mValue = value;
        mControllerId = controllerId;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.AcceptRule.COMMAND);
        writer.beginObject();
        writer.name(Constants.Fields.AcceptRule.NODE_ID).value(mNodeId);
        writer.name(Constants.Fields.AcceptRule.COMMAND_ID).value(mCommandId);
        writer.name(Constants.Fields.AcceptRule.VALUE).value(mValue);
        writer.endObject();
        writer.name(Constants.Fields.AcceptRule.CONTROLLER_ID).value(mControllerId);
    }
}
