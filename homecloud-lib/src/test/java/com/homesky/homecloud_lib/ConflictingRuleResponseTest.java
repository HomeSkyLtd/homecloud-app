package com.homesky.homecloud_lib;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.response.ConflictingRuleResponse;
import com.homesky.homecloud_lib.model.response.RuleResponse;
import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Formatter;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk=23)
public class ConflictingRuleResponseTest {
    @Test
    public void json_isCorrect(){

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: 200, %s: '', %s: {%s: '2', %s: {%s: 1, %s: 3, %s: 4.0}, " +
                        "%s: [[{%s: '1.1', %s: '>', %s: 1}, {%s:'2.1', %s: '>', %s:'3.1'}], [{%s:'1.3', %s: '>', %s: 1.0}]]}}}",
                Constants.Fields.Common.STATUS,
                Constants.Fields.Common.ERROR_MESSAGE,
                Constants.Fields.ConflictingRulesResponse.CONFLICTING_RULE,
                Constants.Fields.ConflictingRulesResponse.CONTROLLER_ID,
                Constants.Fields.ConflictingRulesResponse.COMMAND,
                Constants.Fields.ConflictingRulesResponse.NODE_ID,
                Constants.Fields.ConflictingRulesResponse.COMMAND_ID,
                Constants.Fields.ConflictingRulesResponse.VALUE,
                Constants.Fields.ConflictingRulesResponse.CLAUSES,
                Constants.Fields.ConflictingRulesResponse.LHS,
                Constants.Fields.ConflictingRulesResponse.OPERATOR,
                Constants.Fields.ConflictingRulesResponse.RHS,
                Constants.Fields.ConflictingRulesResponse.LHS,
                Constants.Fields.ConflictingRulesResponse.OPERATOR,
                Constants.Fields.ConflictingRulesResponse.RHS,
                Constants.Fields.ConflictingRulesResponse.LHS,
                Constants.Fields.ConflictingRulesResponse.OPERATOR,
                Constants.Fields.ConflictingRulesResponse.RHS
        );

        ConflictingRuleResponse response = ConflictingRuleResponse.from(sb.toString());
        JSONObject test = null, reference = null;
        try{
            test = new JSONObject(response.toString());
            reference = new JSONObject(f.toString());
        }
        catch(JSONException e){
            e.printStackTrace();
            assert false;
        }
        assertTrue(JSONComparator.equals(test, reference));
    }
}

