package com.homesky.homecloud_lib;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.LogoutRequest;
import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Formatter;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LogoutRequestTest {

    @Test
    public void json_isCorrect(){
        LogoutRequest request = new LogoutRequest("12345");
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: %s, %s: '12345'}",
                Constants.Fields.Common.FUNCTION,
                Constants.Values.Functions.LOGOUT,
                Constants.Fields.Login.TOKEN);
        JSONObject test = null, reference = null;
        try{
            request.writeJSON(writer);
            test = new JSONObject(sw.toString());
            reference = new JSONObject(f.toString());
        }
        catch(JSONException | IOException e){
            e.printStackTrace();
            assert false;
        }
        assertTrue(JSONComparator.equals(test, reference));
    }
}
