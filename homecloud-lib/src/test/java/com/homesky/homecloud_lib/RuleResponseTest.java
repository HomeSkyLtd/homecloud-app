package com.homesky.homecloud_lib;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.response.RuleResponse;
import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RuleResponseTest {
    @Test
    public void json_isCorrect(){

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: 200, %s: '', %s: [{%s: 1, %s: 2, %s: 3, %s: 4.0, " +
                        "%s: [[{%s: '1.1', %s: '>', %s: 1}, {%s:'2.1', %s: '>', %s:'3.1'}], [{%s:'1.3', %s: '>', %s: 1.0}]]}," +
                        "{%s: 5, %s: 6, %s: 7, %s: 8.4," +
                        "%s: [[{%s: '3.2', %s: '>', %s: 1.0}, {%s: '8.1', %s: '>', %s:'3.9'}]]}]}",
                Constants.Fields.Common.STATUS,
                Constants.Fields.Common.ERROR_MESSAGE,
                Constants.Fields.RuleResponse.RULES,
                Constants.Fields.RuleResponse.NODE_ID,
                Constants.Fields.RuleResponse.CONTROLLER_ID,
                Constants.Fields.RuleResponse.COMMAND_ID,
                Constants.Fields.RuleResponse.VALUE,
                Constants.Fields.RuleResponse.CLAUSES,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS,
                Constants.Fields.RuleResponse.NODE_ID,
                Constants.Fields.RuleResponse.CONTROLLER_ID,
                Constants.Fields.RuleResponse.COMMAND_ID,
                Constants.Fields.RuleResponse.VALUE,
                Constants.Fields.RuleResponse.CLAUSES,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS);

        RuleResponse response = RuleResponse.from(sb.toString());
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
