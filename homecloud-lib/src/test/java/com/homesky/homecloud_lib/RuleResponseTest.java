package com.homesky.homecloud_lib;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.Rule;
import com.homesky.homecloud_lib.model.enums.OperatorEnum;
import com.homesky.homecloud_lib.model.response.RuleResponse;
import com.homesky.homecloud_lib.util.JSONComparator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk=23)
public class RuleResponseTest {
    @Test
    public void json_isCorrect(){

        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        f.format("{%s: 200, %s: '', %s: [{%s: '2', %s: {%s: 1, %s: 3, %s: 4.0}, " +
                        "%s: [[{%s: '1.1', %s: '>', %s: 1}, {%s:'2.1', %s: '<=', %s:'3.1'}], [{%s:'1.3', %s: '==', %s: 1.0}]]}," +
                        "{%s: '6', %s: {%s: 5, %s: 7, %s: 8.4}," +
                        "%s: [[{%s: '3.2', %s: '>', %s: 1.0}, {%s: '8.1', %s: '<', %s:'3.9'}]]}]}",
                Constants.Fields.Common.STATUS,
                Constants.Fields.Common.ERROR_MESSAGE,
                Constants.Fields.RuleResponse.RULES,
                Constants.Fields.RuleResponse.CONTROLLER_ID,
                Constants.Fields.RuleResponse.COMMAND,
                Constants.Fields.RuleResponse.NODE_ID,
                Constants.Fields.RuleResponse.COMMAND_ID,
                Constants.Fields.RuleResponse.VALUE,
                Constants.Fields.RuleResponse.CLAUSES,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS,
                Constants.Fields.RuleResponse.CONTROLLER_ID,
                Constants.Fields.RuleResponse.COMMAND,
                Constants.Fields.RuleResponse.NODE_ID,
                Constants.Fields.RuleResponse.COMMAND_ID,
                Constants.Fields.RuleResponse.VALUE,
                Constants.Fields.RuleResponse.CLAUSES,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS,
                Constants.Fields.RuleResponse.LHS,
                Constants.Fields.RuleResponse.OPERATOR,
                Constants.Fields.RuleResponse.RHS);

        RuleResponse response = RuleResponse.from(sb.toString());

        System.out.println("Parsed JSON:");
        System.out.println(response.toString());

        List<Rule> rules = response.getRules();
        Rule rule = rules.get(0);
        assertTrue(rule.getControllerId().equals("2"));
        assertTrue(rule.getCommand().getNodeId() == 1);
        assertTrue(rule.getCommand().getCommandId() == 3);
        assertTrue(rule.getCommand().getValue().equals(new BigDecimal("4.0")));

        List<List<Proposition>> clause = rule.getClause();
        assertTrue(clause.get(0).get(0).getLhs().equals("1.1"));
        assertTrue(clause.get(0).get(0).getRhs().equals("1"));
        assertFalse(clause.get(0).get(0).isLhsValue());
        assertTrue(clause.get(0).get(0).isRhsValue());
        assertTrue(clause.get(0).get(0).getOperator().equals(OperatorEnum.GT));

        assertTrue(clause.get(0).get(1).getLhs().equals("2.1"));
        assertTrue(clause.get(0).get(1).getRhs().equals("3.1"));
        assertFalse(clause.get(0).get(1).isLhsValue());
        assertFalse(clause.get(0).get(1).isRhsValue());
        assertTrue(clause.get(0).get(1).getOperator().equals(OperatorEnum.LE));

        assertTrue(clause.get(1).get(0).getLhs().equals("1.3"));
        assertTrue(clause.get(1).get(0).getRhs().equals("1.0"));
        assertFalse(clause.get(1).get(0).isLhsValue());
        assertTrue(clause.get(1).get(0).isRhsValue());
        assertTrue(clause.get(1).get(0).getOperator().equals(OperatorEnum.EQ));

        rule = rules.get(1);
        assertTrue(rule.getControllerId().equals("6"));
        assertTrue(rule.getCommand().getNodeId() == 5);
        assertTrue(rule.getCommand().getCommandId() == 7);
        assertTrue(rule.getCommand().getValue().equals(new BigDecimal("8.4")));

        clause = rule.getClause();
        assertTrue(clause.get(0).get(0).getLhs().equals("3.2"));
        assertTrue(clause.get(0).get(0).getRhs().equals("1.0"));
        assertFalse(clause.get(0).get(0).isLhsValue());
        assertTrue(clause.get(0).get(0).isRhsValue());
        assertTrue(clause.get(0).get(0).getOperator().equals(OperatorEnum.GT));

        assertTrue(clause.get(0).get(1).getLhs().equals("8.1"));
        assertTrue(clause.get(0).get(1).getRhs().equals("3.9"));
        assertFalse(clause.get(0).get(1).isLhsValue());
        assertFalse(clause.get(0).get(1).isRhsValue());
        assertTrue(clause.get(0).get(1).getOperator().equals(OperatorEnum.LT));
    }
}
