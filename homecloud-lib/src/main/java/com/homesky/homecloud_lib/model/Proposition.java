package com.homesky.homecloud_lib.model;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;

import java.io.IOException;
import java.math.BigDecimal;

public class Proposition {
    private String mLhs, mRhs, mOperator;
    private boolean mIsLhsValue, mIsRhsValue;

    public Proposition(String operator, String lhs, String rhs) {
        mOperator = operator;
        mLhs = lhs;
        mRhs = rhs;
    }

    public Proposition(String operator, BigDecimal lhs, String rhs) {
        this(operator, lhs.toString(), rhs);
        mIsLhsValue = true;
    }

    public Proposition(String operator, String lhs, BigDecimal rhs) {
        this(operator, lhs, rhs.toString());
        mIsRhsValue = true;

    }

    public Proposition(String operator, BigDecimal lhs, BigDecimal rhs) {
        this(operator, lhs.toString(), rhs.toString());
        mIsLhsValue = true;
        mIsRhsValue = true;
    }

    public Proposition(String operator, Object lhs, Object rhs){
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

    public String getLhs() {
        return mLhs;
    }

    public void setLhs(String lhs) {
        mLhs = lhs;
    }

    public String getRhs() {
        return mRhs;
    }

    public void setRhs(String rhs) {
        mRhs = rhs;
    }

    public String getOperator() {
        return mOperator;
    }

    public void setOperator(String operator) {
        mOperator = operator;
    }

    public boolean isLhsValue() {
        return mIsLhsValue;
    }

    public void setLhsValue(boolean lhsValue) {
        mIsLhsValue = lhsValue;
    }

    public boolean isRhsValue() {
        return mIsRhsValue;
    }

    public void setRhsValue(boolean rhsValue) {
        mIsRhsValue = rhsValue;
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

        writer.name(Constants.Fields.RuleResponse.OPERATOR).value(mOperator);
        writer.endObject();
    }
}
