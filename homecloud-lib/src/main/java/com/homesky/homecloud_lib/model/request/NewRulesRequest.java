package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.Rule;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class NewRulesRequest extends RequestModel {

    List<Rule> mRules;

    public NewRulesRequest(List<Rule> rules) {
        super(Constants.Values.Functions.NEW_RULES);
        mRules = rules;
    }

    @Override
    void writeContentsJSON(JsonWriter writer) throws IOException {
        writer.name(Constants.Fields.NewRules.RULES);
        writer.beginArray();
        for(Rule rule : mRules){
            rule.writeJSON(writer);
        }
        writer.endArray();
    }
}
