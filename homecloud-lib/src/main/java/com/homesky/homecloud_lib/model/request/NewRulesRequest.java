package com.homesky.homecloud_lib.model.request;

import android.util.JsonWriter;

import com.homesky.homecloud_lib.model.Constants;
import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.Rule;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Request representing the newRules function, as defined in the HomeCloud protocol.
 */
public class NewRulesRequest extends RequestModel {

    List<Rule> mRules;

    /**
     * Base constructor.
     * @param rules A list of Rule objects to be added.
     */
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
