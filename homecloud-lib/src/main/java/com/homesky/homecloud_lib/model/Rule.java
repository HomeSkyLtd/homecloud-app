package com.homesky.homecloud_lib.model;

import android.util.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Rule{
    int mNodeId, mCommandId;
    String mControllerId;
    BigDecimal mValue;
    List<List<Proposition>> mClause;

    public Rule(int nodeId, String controllerId, int commandId,
                BigDecimal value, List<List<Proposition>> clause) {
        mNodeId = nodeId;
        mControllerId = controllerId;
        mCommandId = commandId;
        mValue = value;
        mClause = clause;
    }

    public int getNodeId() {
        return mNodeId;
    }

    public void setNodeId(int nodeId) {
        mNodeId = nodeId;
    }

    public int getCommandId() {
        return mCommandId;
    }

    public void setCommandId(int commandId) {
        mCommandId = commandId;
    }

    public String getControllerId() {
        return mControllerId;
    }

    public void setControllerId(String controllerId) {
        mControllerId = controllerId;
    }

    public BigDecimal getValue() {
        return mValue;
    }

    public void setValue(BigDecimal value) {
        mValue = value;
    }

    public List<List<Proposition>> getClause() {
        return mClause;
    }

    public void setClause(List<List<Proposition>> clause) {
        mClause = clause;
    }

    public void writeJSON(JsonWriter writer) throws IOException {
        writer.beginObject();
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
        writer.endObject();
    }
}
