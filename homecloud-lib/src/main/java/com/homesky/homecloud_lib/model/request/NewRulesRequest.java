package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.Proposition;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class NewRulesRequest extends RequestModel {

    int mNodeId, mControllerId, mCommandId;
    BigDecimal mValue;
    List<List<Proposition>> mClause;

    public NewRulesRequest(int nodeId, int controllerId, int commandId,
                           BigDecimal value, List<List<Proposition>> clause) {
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
        for(List<Proposition> orStatement : mClause){
            writer.beginArray();
            for(Proposition proposition : orStatement){
                proposition.writeJSON(writer);
            }
            writer.endArray();
        }
        writer.endArray();
    }
}
