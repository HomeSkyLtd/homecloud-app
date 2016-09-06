package com.homesky.homecloud_lib;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.request.NewRulesRequest;
import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NewRulesTest {

    @Test
    public void json_isCorrect(){
        List<List<Proposition>> clause = new ArrayList<>();
        Proposition[] orStatement1 = new Proposition[] {
                new Proposition(">", "1.1", new BigDecimal(1)),
                new Proposition(">", "2.1", "3.1")};
        Proposition[] orStatement2 = new Proposition[] {new Proposition(">", "1.3", new BigDecimal(4.3))};
        clause.add(Arrays.asList(orStatement1));
        clause.add(Arrays.asList(orStatement2));

        NewRulesRequest request = new NewRulesRequest(1, 2, 3, new BigDecimal(4), clause);

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: %s, %s: 1, %s: 2, %s: 3, %s: 4, " +
                "%s: [[{%s: '1.1', %s: '>', %s: 1}, {%s: '2.1', %s: '>', %s: '3.1'}], [{%s: '1.3', %s: '>', %s: 4.3}]]}",
                Constants.Fields.Common.FUNCTION,
                Constants.Values.Functions.NEW_RULES,
                Constants.Fields.NewRules.NODE_ID,
                Constants.Fields.NewRules.CONTROLLER_ID,
                Constants.Fields.NewRules.COMMAND_ID,
                Constants.Fields.NewRules.VALUE,
                Constants.Fields.NewRules.CLAUSES,
                Constants.Fields.NewRules.LHS,
                Constants.Fields.NewRules.OPERATOR,
                Constants.Fields.NewRules.RHS,
                Constants.Fields.NewRules.LHS,
                Constants.Fields.NewRules.OPERATOR,
                Constants.Fields.NewRules.RHS,
                Constants.Fields.NewRules.LHS,
                Constants.Fields.NewRules.OPERATOR,
                Constants.Fields.NewRules.RHS
                );
        JSONObject test = null, reference = null;
        try{
            test = new JSONObject(request.toString());
            reference = new JSONObject(f.toString());
        }
        catch(JSONException e){
            e.printStackTrace();
            assert false;
        }
        assertTrue(JSONComparator.equals(test, reference));
    }
}
