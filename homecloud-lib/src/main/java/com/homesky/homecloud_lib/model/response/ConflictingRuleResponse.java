package com.homesky.homecloud_lib.model.response;

import android.util.JsonWriter;
import android.util.Log;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.Rule;
import com.homesky.homecloud_lib.model.enums.OperatorEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a response from the server containing information about possible conflicting rules.
 */
public class ConflictingRuleResponse extends SimpleResponse{
    private static final String TAG = "ConflictingRuleResp";
    private Rule mConflictingRule;

    protected ConflictingRuleResponse(int status, String errorMessage, Rule conflictingRule) {
        super(status, errorMessage);
        mConflictingRule = conflictingRule;
    }

    /**
     * Gets the conflicting rule. Returns null if there is no conflicting rule.
     * @return A {@link Rule} object representing the conflicting rule.
     */
    public Rule getConflictingRule() {
        return mConflictingRule;
    }

    /**
     * Creates a {@link ConflictingRuleResponse} instance from the JSON payload received as response from the server.
     * @param jsonStr The JSON response in string format.
     * @return A {@link ConflictingRuleResponse} object representing the response.
     */
    public static ConflictingRuleResponse from(String jsonStr){
        if (jsonStr == null) return null;

        Rule conflictingRule = null;
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int status = obj.getInt(Constants.Fields.Common.STATUS);
            String errorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);

            if (status == 200) {
                if(obj.has(Constants.Fields.ConflictingRulesResponse.CONFLICTING_RULE)){
                    JSONObject conflictingRuleObj =
                            obj.getJSONObject(Constants.Fields.ConflictingRulesResponse.CONFLICTING_RULE);

                    JSONObject commandObj = conflictingRuleObj.getJSONObject(Constants.Fields.ConflictingRulesResponse.COMMAND);
                    int nodeId =  commandObj.getInt(Constants.Fields.ConflictingRulesResponse.NODE_ID);
                    int commandId = commandObj.getInt(Constants.Fields.ConflictingRulesResponse.COMMAND_ID);
                    BigDecimal value = new BigDecimal(commandObj.getString(Constants.Fields.ConflictingRulesResponse.VALUE));
                    String controllerId = conflictingRuleObj.getString(Constants.Fields.ConflictingRulesResponse.CONTROLLER_ID);

                    List<List<Proposition>> clause = new ArrayList<>();
                    JSONArray andStatementJSON = conflictingRuleObj.getJSONArray(Constants.Fields.ConflictingRulesResponse.CLAUSES);
                    for(int i = 0 ; i < andStatementJSON.length() ; ++i) {
                        List<Proposition> orStatement = new ArrayList<>();
                        JSONArray orStatementJSON = andStatementJSON.getJSONArray(i);
                        for(int j = 0 ; j < orStatementJSON.length() ; ++j){
                            JSONObject propositionJSON = orStatementJSON.getJSONObject(j);
                            orStatement.add(new Proposition(
                                    OperatorEnum.fromRepresentation(
                                            propositionJSON.getString(Constants.Fields.ConflictingRulesResponse.OPERATOR)),
                                    propositionJSON.get(Constants.Fields.ConflictingRulesResponse.LHS),
                                    propositionJSON.get(Constants.Fields.ConflictingRulesResponse.RHS)
                            ));
                        }
                        clause.add(orStatement);
                    }
                    conflictingRule = new Rule(nodeId, controllerId, commandId, value, clause);
                }
            }
            return new ConflictingRuleResponse(status, errorMessage, conflictingRule);
        }
        catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void writeJSON(JsonWriter writer) throws IOException {
        super.writeJSON(writer);
        if(mConflictingRule != null){
            writer.name(Constants.Fields.ConflictingRulesResponse.CONFLICTING_RULE);
            mConflictingRule.writeJSON(writer);
        }
    }
}
