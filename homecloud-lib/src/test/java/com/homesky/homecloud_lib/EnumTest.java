package com.homesky.homecloud_lib;

import com.homesky.homecloud_lib.model.enums.EnumUtil;
import com.homesky.homecloud_lib.model.enums.MeasureStrategyEnum;
import com.homesky.homecloud_lib.model.enums.NodeClassEnum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.EnumSet;

import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk=23)
public class EnumTest {

    @Test
    public void multipleEnum_isCorrect() {
        EnumSet<NodeClassEnum> set = EnumUtil.setFrom(1, NodeClassEnum.class);
        assertTrue(set.size() == 1);
        assertTrue(set.contains(NodeClassEnum.SENSOR));

        set = EnumUtil.setFrom(2, NodeClassEnum.class);
        assertTrue(set.size() == 1);
        assertTrue(set.contains(NodeClassEnum.ACTUATOR));

        set = EnumUtil.setFrom(3, NodeClassEnum.class);
        assertTrue(set.size() == 2);
        assertTrue(set.contains(NodeClassEnum.SENSOR));
        assertTrue(set.contains(NodeClassEnum.ACTUATOR));

        MeasureStrategyEnum ms = EnumUtil.enumFrom(1, MeasureStrategyEnum.class);
        assertTrue(ms.equals(MeasureStrategyEnum.EVENT));

        ms = EnumUtil.enumFrom(2, MeasureStrategyEnum.class);
        assertTrue(ms.equals(MeasureStrategyEnum.PERIODIC));
    }
}
