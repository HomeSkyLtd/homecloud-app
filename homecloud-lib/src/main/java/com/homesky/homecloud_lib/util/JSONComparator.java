package com.homesky.homecloud_lib.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONComparator {
    public static boolean equals(JSONObject obj1, JSONObject obj2){
        if(obj1.length() != obj2.length())
            return false;

        List<String> keys1 = getJSONKeys(obj1);

        for(String key : keys1){
            try{
                Object val1 = obj1.get(key);
                Object val2 = obj2.get(key);
                if(!compareValues(val1, val2))
                    return false;
            }
            catch(JSONException je){
                return false;
            }
        }
        return true;
    }

    private static boolean equals(JSONArray arr1, JSONArray arr2) throws JSONException{
        if(arr1.length() != arr2.length())
            return false;

        for(int i = 0 ; i < arr1.length() ; ++i){
            if(!compareValues(arr1.get(i), arr2.get(i))) return false;
        }

        return true;
    }

    public static List<String> getJSONKeys(JSONObject obj){
        List<String> keys = new ArrayList<>(obj.length());
        Iterator<String> iter = obj.keys();
        while(iter.hasNext()){
            keys.add(iter.next());
        }
        return keys;
    }

    private static boolean compareValues(Object val1, Object val2) throws JSONException{
        if(val1 instanceof JSONObject){
            if(val2 instanceof JSONObject)
                return equals((JSONObject)val1, (JSONObject)val2);
            else return false;
        } else if (val1 instanceof JSONArray){
            if(val2 instanceof JSONArray)
                return equals((JSONArray)val1, (JSONArray)val2);
            else return false;
        } else {
            return val1.equals(val2);
        }

    }
}
