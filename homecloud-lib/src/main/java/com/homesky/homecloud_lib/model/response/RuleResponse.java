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
 * Represents a response from the server including rules information.
 */
public class RuleResponse extends SimpleResponse {
    private static final String TAG = "RuleResponse";

    private List<Rule> mRules;

    protected RuleResponse(int status, String errorMessage, List<Rule> rules) {
        super(status, errorMessage);
        mRules = rules;
    }

    /**
     * Creates a {@link RuleResponse} instance from the JSON payload received as response from the server.
     * @param jsonStr The JSON response in string format.
     * @return A {@link RuleResponse} object representing the response.
     */
    public static RuleResponse from(String jsonStr) {
        if (jsonStr == null) return null;

        List<Rule> rules = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            int status = obj.getInt(Constants.Fields.Common.STATUS);
            String errorMessage = obj.getString(Constants.Fields.Common.ERROR_MESSAGE);

            if (status == 200) {
                JSONArray rulesJSON = obj.getJSONArray(Constants.Fields.RuleResponse.RULES);
                for (int i = 0; i < rulesJSON.length(); ++i) {
                    JSONObject ruleJSON = rulesJSON.getJSONObject(i);
                    String controllerId = ruleJSON.getString(Constants.Fields.NewRules.CONTROLLER_ID);
                    JSONObject commandJSON = ruleJSON.getJSONObject(Constants.Fields.NewRules.COMMAND);
                    int nodeId = commandJSON.getInt(Constants.Fields.NewRules.NODE_ID);
                    int commandId = commandJSON.getInt(Constants.Fields.NewRules.COMMAND_ID);
                    BigDecimal value = new BigDecimal(commandJSON.getString(Constants.Fields.NewRules.VALUE));
                    List<List<Proposition>> clause = new ArrayList<>();
                    JSONArray clauseJSON = ruleJSON.getJSONArray(Constants.Fields.NewRules.CLAUSES);
                    for (int j = 0; j < clauseJSON.length(); ++j) {
                        List<Proposition> orStatement = new ArrayList<>();
                        JSONArray orStatementJSON = clauseJSON.getJSONArray(j);
                        for (int k = 0; k < orStatementJSON.length(); ++k) {
                            JSONObject propositionJSON = orStatementJSON.getJSONObject(k);
                            orStatement.add(new Proposition(
                                    OperatorEnum.fromRepresentation(
                                            propositionJSON.getString(Constants.Fields.RuleResponse.OPERATOR)),
                                    propositionJSON.get(Constants.Fields.RuleResponse.LHS),
                                    propositionJSON.get(Constants.Fields.RuleResponse.RHS)));
                            //orStatement.add(orStatementJSON.getString(k));
                        }
                        clause.add(orStatement);
                    }
                    Rule rule = new Rule(nodeId, controllerId, commandId, value, clause);
                    rules.add(rule);
                }
            }

            return new RuleResponse(status, errorMessage, rules);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
            return null;
        }
    }

    public List<Rule> getRules() {
        return mRules;
    }

    @Override
    protected void writeJSON(JsonWriter writer) throws IOException {
        super.writeJSON(writer);
        writer.name(Constants.Fields.RuleResponse.RULES);
        writer.beginArray();
        for (Rule rule : mRules) {
            rule.writeJSON(writer);
        }
        writer.endArray();
    }
}


