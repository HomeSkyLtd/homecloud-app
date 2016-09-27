package com.homesky.homecloud_lib;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.response.StateResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk=23)
public class StateResponseTest {
    @Test
    public void json_isCorrect() {

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: 200, %s: '', %s:[{%s: 1, %s: '1', %s: {'1': 1.1, '2': 0}, %s: {'1': 2.5, '3': 1}}]}",
                Constants.Fields.Common.STATUS,
                Constants.Fields.Common.ERROR_MESSAGE,
                Constants.Fields.GetHouseState.STATE,
                Constants.Fields.GetHouseState.NODE_ID,
                Constants.Fields.GetHouseState.CONTROLLER_ID,
                Constants.Fields.GetHouseState.DATA,
                Constants.Fields.GetHouseState.COMMAND
        );

        StateResponse response = StateResponse.from(f.toString());

        System.out.println("Parsed JSON:");
        System.out.println(response.toString());

        StateResponse.NodeState state = response.getState().get(0);
        assertTrue(state.getNodeId() == 1);
        assertTrue(state.getControllerId().equals("1"));

        Map<Integer, BigDecimal> data = state.getData();
        assertTrue(data.get(1).equals(new BigDecimal("1.1")));
        assertTrue(data.get(2).equals(new BigDecimal("0")));

        Map<Integer, BigDecimal> command = state.getCommand();
        assertTrue(command.get(1).equals(new BigDecimal("2.5")));
        assertTrue(command.get(3).equals(new BigDecimal("1")));
    }
}
