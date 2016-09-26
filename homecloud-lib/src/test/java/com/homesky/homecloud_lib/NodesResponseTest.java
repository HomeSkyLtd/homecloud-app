package com.homesky.homecloud_lib;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.enums.DataCategoryEnum;
import com.homesky.homecloud_lib.model.enums.MeasureStrategyEnum;
import com.homesky.homecloud_lib.model.enums.NodeClassEnum;
import com.homesky.homecloud_lib.model.enums.TypeEnum;
import com.homesky.homecloud_lib.model.response.NodesResponse;
import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk=23)
public class NodesResponseTest {

    @Test
    public void json_isCorrect(){

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: 200, %s: '', %s: [{%s: 1, %s: '2', %s: 3, %s: 0, %s: 1, %s: {name: 'nó1'}," +
                "%s: [{%s: 5, %s: 1, %s: 2, %s: [1, 3], %s: 2, %s: 'kg'}], " +
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

        NodesResponse response = NodesResponse.from(sb.toString());

        System.out.println("Parsed JSON:");
        System.out.println(response.toString());

        NodesResponse.Node node = response.getNodes().get(0);
        assertTrue(node.getNodeId() == 1);
        assertTrue(node.getControllerId().equals("2"));
        assertTrue(node.getNodeClass().contains(NodeClassEnum.ACTUATOR));
        assertTrue(node.getNodeClass().contains(NodeClassEnum.SENSOR));
        assertTrue(node.getAccepted() == 0);
        assertTrue(node.getAlive() == 1);
        assertTrue(node.getExtra().get("name").equals("nó1") );

        NodesResponse.DataType dataType = node.getDataType().get(0);
        assertTrue(dataType.getId() == 5);
        assertTrue(dataType.getMeasureStrategy().equals(MeasureStrategyEnum.EVENT));
        assertTrue(dataType.getType().equals(TypeEnum.BOOL));
        assertTrue(dataType.getRange()[0].equals(new BigDecimal(1)));
        assertTrue(dataType.getRange()[1].equals(new BigDecimal(3)));
        assertTrue(dataType.getDataCategory().equals(DataCategoryEnum.LUMINANCE));
        assertTrue(dataType.getUnit().equals("kg"));
    }
}
