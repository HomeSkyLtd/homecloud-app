package com.homesky.homecloud_lib.model.response;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RuleResponse extends SimpleResponse{
    private static final String TAG = "RuleResponse";

    private List<Rule> mRules;

    protected RuleResponse(int status, String errorMessage, List<Rule> rules) {
        super(status, errorMessage);
        mRules = rules;
    }

    public static RuleResponse from(String jsonStr){
        if(jsonStr == null) return null;

        List<Rule> rules = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int status = obj.getInt(Constants.Fields.Common.STATUS);
            String errorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);

            JSONArray rulesJSON = obj.getJSONArray(Constants.Fields.RuleResponse.RULES);
            for(int i = 0 ; i < rulesJSON.length() ; ++i){
                JSONObject ruleJSON = rulesJSON.getJSONObject(i);
                String nodeId = ruleJSON.getString(Constants.Fields.NewRules.NODE_ID);
                String controllerId = ruleJSON.getString(Constants.Fields.NewRules.CONTROLLER_ID);
                String commandId = ruleJSON.getString(Constants.Fields.NewRules.COMMAND_ID);
                String value = ruleJSON.getString(Constants.Fields.NewRules.VALUE);
                List<List<String>> clause = new ArrayList<>();
                JSONArray clauseJSON = ruleJSON.getJSONArray(Constants.Fields.NewRules.CLAUSES);
                for(int j = 0 ; j < clauseJSON.length() ; ++j){
                    List<String> orStatement = new ArrayList<>();
                    JSONArray orStatementJSON = clauseJSON.getJSONArray(j);
                    for(int k = 0 ; k < orStatementJSON.length() ; ++k)
                        orStatement.add(orStatementJSON.getString(k));
                    clause.add(orStatement);
                }
                Rule rule = new Rule(nodeId, controllerId, commandId, value, clause);
                rules.add(rule);
            }

            return new RuleResponse(status, errorMessage, rules);
        }
        catch (JSONException e){
            Log.e(TAG, "Error parsing JSON", e);
            return null;
        }
    }

    @Override
    protected void writeJSON(JsonWriter writer) throws IOException {
        super.writeJSON(writer);
        writer.name(Constants.Fields.RuleResponse.RULES);
        writer.beginArray();
        for(Rule rule : mRules){
            writer.beginObject();
            writer.name(Constants.Fields.NewRules.NODE_ID).value(rule.mNodeId);
            writer.name(Constants.Fields.NewRules.CONTROLLER_ID).value(rule.mControllerId);
            writer.name(Constants.Fields.NewRules.COMMAND_ID).value(rule.mCommandId);
            writer.name(Constants.Fields.NewRules.VALUE).value(rule.mValue);
            writer.name(Constants.Fields.NewRules.CLAUSES);
            writer.beginArray();
            for(List<String> orStatement : rule.mClause){
                writer.beginArray();
                for(String proposition : orStatement){
                    writer.value(proposition);
                }
                writer.endArray();
            }
            writer.endArray();
            writer.endObject();
        }
        writer.endArray();
    }

    public static class Rule{
        String mNodeId, mControllerId, mCommandId, mValue;
        List<List<String>> mClause;

        public Rule(String nodeId, String controllerId, String commandId, String value, List<List<String>> clause) {
            mNodeId = nodeId;
            mControllerId = controllerId;
            mCommandId = commandId;
            mValue = value;
            mClause = clause;
        }
    }
}
