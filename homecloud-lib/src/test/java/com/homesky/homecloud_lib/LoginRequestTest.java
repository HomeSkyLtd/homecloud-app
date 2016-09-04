package com.homesky.homecloud_lib;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.request.LoginRequest;
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
public class LoginRequestTest {

    @Test
    public void json_isCorrect(){
        LoginRequest request = new LoginRequest("user", "pass", "12345");

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: %s, %s: 'user', %s: 'pass', %s: '12345'}",
                Constants.Fields.Common.FUNCTION,
                Constants.Values.Functions.LOGIN,
                Constants.Fields.Login.USERNAME,
                Constants.Fields.Login.PASSWORD,
                Constants.Fields.Login.TOKEN);
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
