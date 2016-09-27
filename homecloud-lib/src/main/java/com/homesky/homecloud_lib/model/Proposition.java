package com.homesky.homecloud_lib.model;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.enums.OperatorEnum;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Represents a logic proposition. A proposition is an expression in the form "LHS Operator RHS"
 */
public class Proposition {
    private String mLhs, mRhs;
    private OperatorEnum mOperator;
    private boolean mIsLhsValue, mIsRhsValue;

    /**
     * Builds a proposition.
     * @param operator The operator (<, >, <=, >=, ==, !=).
     * @param lhs The left-hand side of the expression (another node id).
     * @param rhs The left-hand side of the expression (another node id).
     */
    public Proposition(OperatorEnum operator, String lhs, String rhs) {
        mOperator = operator;
        mLhs = lhs;
        mRhs = rhs;
    }

    /**
     * Builds a proposition.
     * @param operator The operator (<, >, <=, >=, ==, !=).
     * @param lhs The left-hand side of the expression (a numeric value).
     * @param rhs The left-hand side of the expression (another node id).
     */
    public Proposition(OperatorEnum operator, BigDecimal lhs, String rhs) {
        this(operator, lhs.toString(), rhs);
        mIsLhsValue = true;
    }

    /**
     * Builds a proposition.
     * @param operator The operator (<, >, <=, >=, ==, !=).
     * @param lhs The left-hand side of the expression (another node id).
     * @param rhs The left-hand side of the expression (a numeric value).
     */
    public Proposition(OperatorEnum operator, String lhs, BigDecimal rhs) {
        this(operator, lhs, rhs.toString());
        mIsRhsValue = true;

    }

    /**
     * Builds a proposition.
     * @param operator The operator (<, >, <=, >=, ==, !=).
     * @param lhs The left-hand side of the expression (a numeric value).
     * @param rhs The left-hand side of the expression (a numeric value).
     */
    public Proposition(OperatorEnum operator, BigDecimal lhs, BigDecimal rhs) {
        this(operator, lhs.toString(), rhs.toString());
        mIsLhsValue = true;
        mIsRhsValue = true;
    }

    public Proposition(OperatorEnum operator, Object lhs, Object rhs){
        mOperator = operator;
        if(lhs instanceof String)
            mLhs = (String)lhs;
        else{
            mLhs = ((Number)lhs).toString();
            mIsLhsValue = true;
        }
        if(rhs instanceof String)
            mRhs = (String)rhs;
        else{
            mRhs = ((Number)rhs).toString();
            mIsRhsValue = true;
        }
    }

    /**
     * Returns the left-hand side of the expression.
     * @return The LHS of the expression.
     */
    public String getLhs() {
        return mLhs;
    }

    /**
     * Sets the left-hand side of the expression as another node id.
     * @param lhs The node id representing the LHS of the expression.
     */
    public void setLhs(String lhs) {
        mLhs = lhs;
        mIsLhsValue = false;
    }

    /**
     * Sets the left-hand side of the expression as a numeric value.
     * @param lhs The numeric value representing the LHS of the expression.
     */
    public void setLhs(Number lhs) {
        mLhs = lhs.toString();
        mIsLhsValue = false;
    }

    /**
     * Returns the right-hand side of the expression.
     * @return The RHS of the expression.
     */
    public String getRhs() {
        return mRhs;
    }

    /**
     * Sets the right-hand side of the expression as another node id.
     * @param rhs The node id representing the RHS of the expression.
     */
    public void setRhs(String rhs) {
        mRhs = rhs;
    }

    /**
     * Sets the right-hand side of the expression as a numeric value.
     * @param rhs The numeric value representing the RHS of the expression.
     */
    public void setRhs(Number rhs) {
        mLhs = rhs.toString();
        mIsLhsValue = false;
    }

    /**
     * Gets the operator associated to the proposition.
     * @return The operator associated to the proposition.
     */
    public OperatorEnum getOperator() {
        return mOperator;
    }

    /**
     * Sets the operator of the proposition.
     * @param operator The operator to be used in the proposition.
     */
    public void setOperator(OperatorEnum operator) {
        mOperator = operator;
    }

    /**
     * Returns whether the LHS is a numeric value or the id of another node.
     * @return true if the LHS is a numeric value, or false if it refers to a node id.
     */
    public boolean isLhsValue() {
        return mIsLhsValue;
    }

    /**
     * Returns whether the RHS is a numeric value or the id of another node.
     * @return true if the RHS is a numeric value, or false if it refers to a node id.
     */

    public boolean isRhsValue() {
        return mIsRhsValue;
    }

    public void writeJSON(JsonWriter writer) throws IOException {
        writer.beginObject();
        if (mIsLhsValue)
            writer.name(Constants.Fields.RuleResponse.LHS).value(new BigDecimal(mLhs));
        else
            writer.name(Constants.Fields.RuleResponse.LHS).value(mLhs);
        if (mIsRhsValue)
            writer.name(Constants.Fields.RuleResponse.RHS).value(new BigDecimal(mRhs));
        else
            writer.name(Constants.Fields.RuleResponse.RHS).value(mRhs);

        writer.name(Constants.Fields.RuleResponse.OPERATOR).value(mOperator.getRepresentation());
        writer.endObject();
    }
}
