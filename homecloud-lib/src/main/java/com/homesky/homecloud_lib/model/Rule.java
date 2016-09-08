package com.homesky.homecloud_lib.model;

import android.util.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Rule{
    String mControllerId;
    List<List<Proposition>> mClause;
    Command mCommand;

    public Rule(int nodeId, String controllerId, int commandId,
                BigDecimal value, List<List<Proposition>> clause) {
        mCommand = new Command(nodeId, commandId, value);
        mControllerId = controllerId;
        mClause = clause;
    }

    public String getControllerId() {
        return mControllerId;
    }

    public void setControllerId(String controllerId) {
        mControllerId = controllerId;
    }

    public List<List<Proposition>> getClause() {
        return mClause;
    }

    public void setClause(List<List<Proposition>> clause) {
        mClause = clause;
    }

    public void writeJSON(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name(Constants.Fields.NewRules.COMMAND);
        writer.beginObject();
        writer.name(Constants.Fields.NewRules.NODE_ID).value(mCommand.getNodeId());
        writer.name(Constants.Fields.NewRules.COMMAND_ID).value(mCommand.getCommandId());
        writer.name(Constants.Fields.NewRules.VALUE).value(mCommand.getValue());
        writer.endObject();
        writer.name(Constants.Fields.NewRules.CONTROLLER_ID).value(mControllerId);
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

    public static class Command{
        private int mNodeId, mCommandId;
        private BigDecimal mValue;

        public Command(int nodeId, int commandId, BigDecimal value) {
            mNodeId = nodeId;
            mCommandId = commandId;
            mValue = value;
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

        public BigDecimal getValue() {
            return mValue;
        }

        public void setValue(BigDecimal value) {
            mValue = value;
        }
    }
}
