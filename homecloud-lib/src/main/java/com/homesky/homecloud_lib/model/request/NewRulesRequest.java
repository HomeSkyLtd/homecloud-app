package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;
import java.util.List;

public class NewRulesRequest extends RequestModel {

    String mNodeId, mControllerId, mCommandId, mValue;
    List<List<String>> mClause;

    public NewRulesRequest(String nodeId, String controllerId, String commandId,
                           String value, List<List<String>> clause) {
        super(Constants.Values.Functions.NEW_RULES);
        mNodeId = nodeId;
        mControllerId = controllerId;
        mCommandId = commandId;
        mValue = value;
        mClause = clause;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.NewRules.NODE_ID).value(mNodeId);
        writer.name(Constants.Fields.NewRules.CONTROLLER_ID).value(mControllerId);
        writer.name(Constants.Fields.NewRules.COMMAND_ID).value(mCommandId);
        writer.name(Constants.Fields.NewRules.VALUE).value(mValue);
        writer.name(Constants.Fields.NewRules.CLAUSES);
        writer.beginArray();
        for(List<String> orStatement : mClause){
            writer.beginArray();
            for(String proposition : orStatement){
                writer.value(proposition);
            }
            writer.endArray();
        }
        writer.endArray();
    }
}
