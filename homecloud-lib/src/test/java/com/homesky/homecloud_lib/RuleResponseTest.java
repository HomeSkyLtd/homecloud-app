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
        f.format("{%s: 200, %s: '', %s: [{%s: '1', %s: '2', %s: '3', %s: '4', " +
                        "%s: [[\"'1.1' > 1\", \"'2.1' > '3.1'\"], [\"'1.3' > 4\"]]}," +
                        "{%s: '5', %s: '6', %s: '7', %s: '8'," +
                        "%s: [[\"'3.2' > 14\", \"'8.1' > '3.9'\"], [\"'10.3' > 4\"]]}]}",
                Constants.Fields.Common.STATUS,
                Constants.Fields.Common.ERROR_MESSAGE,
                Constants.Fields.RuleResponse.RULES,
                Constants.Fields.NewRules.NODE_ID,
                Constants.Fields.NewRules.CONTROLLER_ID,
                Constants.Fields.NewRules.COMMAND_ID,
                Constants.Fields.NewRules.VALUE,
                Constants.Fields.NewRules.CLAUSES,
                Constants.Fields.NewRules.NODE_ID,
                Constants.Fields.NewRules.CONTROLLER_ID,
                Constants.Fields.NewRules.COMMAND_ID,
                Constants.Fields.NewRules.VALUE,
                Constants.Fields.NewRules.CLAUSES);

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
