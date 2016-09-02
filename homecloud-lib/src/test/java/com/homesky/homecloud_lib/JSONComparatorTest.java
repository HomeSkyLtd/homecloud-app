package com.homesky.homecloud_lib;

import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONComparatorTest {
    @Test
    public void equals_isCorrect() throws Exception {
        JSONObject obj1 = new JSONObject("{a: 1}");
        JSONObject obj2 = new JSONObject("{a: 1}");
        assertTrue(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{}");
        obj2 = new JSONObject("{}");
        assertTrue(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: 1}");
        obj2 = new JSONObject("{a: 2}");
        assertFalse(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: 1, b: 2}");
        obj2 = new JSONObject("{b: 2, a: 1}");
        assertTrue(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: 1, b: 2, c: 3}");
        obj2 = new JSONObject("{a: 1, b: 2, c: 3, d: 4}");
        assertFalse(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: 1, b: 2, c: 3, d: 4}");
        obj2 = new JSONObject("{a: 1, b: 2, c: 3}");
        assertFalse(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: [1, 2, 3]}");
        obj2 = new JSONObject("{a: [1, 2, 3]}");
        assertTrue(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: [1, 2, [3, 4]]}");
        obj2 = new JSONObject("{a: [1, 2, [3, 4]]}");
        assertTrue(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: [1, 2, 3, 4]}");
        obj2 = new JSONObject("{a: [1, 2, [3, 4]]}");
        assertFalse(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: [1, 2, 3, 4], b: {c: 3, d: 4}}");
        obj2 = new JSONObject("{a: [1, 2, 3, 4], b: {c: 3, d: 4}}");
        assertTrue(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: [1, 2, {e: 1, f:2}, 4], b: {c: 3, d: 4}}");
        obj2 = new JSONObject("{a: [1, 2, {f:2, e: 1}, 4], b: {c: 3, d: 4}}");
        assertTrue(JSONComparator.equals(obj1, obj2));

        obj1 = new JSONObject("{a: [1, 2, 3, 4]}");
        obj2 = new JSONObject("{a: [1, 3, 2, 4]}");
        assertFalse(JSONComparator.equals(obj1, obj2));
    }
}
