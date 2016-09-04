package com.homesky.homecloud_lib;

import com.homesky.homecloud_lib.model.response.RuleResponse;
import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        JSONObject obj1 = new JSONObject("{a: 1, b:2, c:3}");
        JSONObject obj2 = new JSONObject("{a: 1}");
        System.out.println(JSONComparator.getJSONKeys(obj1));

    }
}