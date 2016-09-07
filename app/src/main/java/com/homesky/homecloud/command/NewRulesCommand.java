package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.model.Proposition;
import com.homesky.homecloud_lib.model.request.NewRulesRequest;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

import java.math.BigDecimal;
import java.util.List;

public class NewRulesCommand implements Command{
    List<NewRulesRequest.Rule> mRules;

    public NewRulesCommand(List<NewRulesRequest.Rule> rules) {
        mRules = rules;
    }

    @Override
    public SimpleResponse execute() {
        return HomecloudHolder.getInstance().newRules(mRules);
    }
}
