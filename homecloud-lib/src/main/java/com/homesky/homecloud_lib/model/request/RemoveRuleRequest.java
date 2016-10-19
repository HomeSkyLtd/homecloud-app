package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.Rule;

import java.io.IOException;

public class RemoveRuleRequest extends RequestModel {

    private Rule mRule;

    public RemoveRuleRequest(Rule rule){
        super(Constants.Values.Functions.REMOVE_RULE);
        mRule = rule;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.RemoveRule.COMMAND);
        writer.beginObject();
        writer.name(Constants.Fields.RemoveRule.COMMAND_ID);
        writer.value(mRule.getCommand().getCommandId());
        writer.name(Constants.Fields.RemoveRule.NODE_ID);
        writer.value(mRule.getCommand().getNodeId());
        writer.name(Constants.Fields.RemoveRule.VALUE);
        writer.value(mRule.getCommand().getValue());
        writer.endObject();
        writer.name(Constants.Fields.RemoveRule.CONTROLLER_ID);
        writer.value(mRule.getControllerId());
    }
}
