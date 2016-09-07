package com.homesky.homecloud_lib;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.response.NodesResponse;
import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Formatter;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NodesResponseTest {

    @Test
    public void json_isCorrect(){

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: 200, %s: '', %s: [{%s: 1, %s: '2', %s: '3', %s: 0, %s: 1, %s: {name: 'nó1'}," +
                "%s: [{%s: 5, %s: 'ms', %s: 't', %s: [1, 3], %s: 'dc', %s: 'kg'}], " +
                "%s: []}]}",
                Constants.Fields.Common.STATUS,
                Constants.Fields.Common.ERROR_MESSAGE,
                Constants.Fields.NodesResponse.NODES,
                Constants.Fields.NodesResponse.NODE_ID,
                Constants.Fields.NodesResponse.CONTROLLER_ID,
                Constants.Fields.NodesResponse.NODE_CLASS,
                Constants.Fields.NodesResponse.ACCEPTED,
                Constants.Fields.NodesResponse.ALIVE,
                Constants.Fields.NodesResponse.EXTRA,
                Constants.Fields.NodesResponse.DATA_TYPE,
                Constants.Fields.NodesResponse.ID,
                Constants.Fields.NodesResponse.MEASURE_STRATEGY,
                Constants.Fields.NodesResponse.TYPE,
                Constants.Fields.NodesResponse.RANGE,
                Constants.Fields.NodesResponse.DATA_CATEGORY,
                Constants.Fields.NodesResponse.UNIT,
                Constants.Fields.NodesResponse.COMMAND_TYPE
                );

//        System.out.println(sb.toString());
        NodesResponse response = NodesResponse.from(sb.toString());
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
