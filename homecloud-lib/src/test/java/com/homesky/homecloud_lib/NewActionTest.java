package com.homesky.homecloud_lib;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.request.NewActionRequest;
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
import java.util.Formatter;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NewActionTest {

    @Test
    public void json_isCorrect(){
        NewActionRequest request = new NewActionRequest(1, 2, 3, new BigDecimal(4));

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: %s, %s: {%s: 1, %s: 2, %s: 3, %s: 4}}",
                Constants.Fields.Common.FUNCTION,
                Constants.Values.Functions.NEW_ACTION,
                Constants.Fields.NewAction.ACTION,
                Constants.Fields.NewAction.NODE_ID,
                Constants.Fields.NewAction.CONTROLLER_ID,
                Constants.Fields.NewAction.COMMAND_ID,
                Constants.Fields.NewAction.VALUE);
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
