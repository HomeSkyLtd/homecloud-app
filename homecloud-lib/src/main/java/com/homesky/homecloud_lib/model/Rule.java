package com.homesky.homecloud_lib.model;

import android.util.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Represents an automation rule.
 */
public class Rule{
    String mControllerId;
    List<List<Proposition>> mClause;
    Command mCommand;

    /**
     * Builds a rule associating a target node, whose state will be affected by the activation of the
     * rule, and the conditions that trigger it.
     * @param nodeId The target node id, whose state will be changed as result of triggering the rule.
     * @param controllerId The target node's controller id.
     * @param commandId The node's command id affected by the rule.
     * @param value The desired value that the node's specified command should take after the rule is
     *              triggered.
     * @param clause The condition to trigger the rule, represented in CNF form (a list of lists containing
     *               {@link Proposition} objects).
     */
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

    public Command getCommand() {
        return mCommand;
    }

    public void setCommand(Command command) {
        mCommand = command;
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
