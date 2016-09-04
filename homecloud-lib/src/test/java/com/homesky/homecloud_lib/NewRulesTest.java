package com.homesky.homecloud_lib;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
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
        List<List<String>> clause = new ArrayList<>();
        String[] orStatement1 = new String[] {"'1.1' > 1", "'2.1' > '3.1'"};
        String[] orStatement2 = new String[] {"'1.3' > 4"};
        clause.add(Arrays.asList(orStatement1));
        clause.add(Arrays.asList(orStatement2));

        NewRulesRequest request = new NewRulesRequest("1", "2", "3", "4", clause);

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: %s, %s: '1', %s: '2', %s: '3', %s: '4', " +
                "%s: [[\"'1.1' > 1\", \"'2.1' > '3.1'\"], [\"'1.3' > 4\"]]}",
                Constants.Fields.Common.FUNCTION,
                Constants.Values.Functions.NEW_RULES,
                Constants.Fields.NewRules.NODE_ID,
                Constants.Fields.NewRules.CONTROLLER_ID,
                Constants.Fields.NewRules.COMMAND_ID,
                Constants.Fields.NewRules.VALUE,
                Constants.Fields.NewRules.CLAUSES);
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
