package com.homesky.homecloud_lib;

import com.homesky.homecloud_lib.model.response.RuleResponse;
import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk=23)
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        JSONObject obj1 = new JSONObject("{value: 1, b:2, c:3}");
        System.out.println(obj1.getString("value"));
    }
}