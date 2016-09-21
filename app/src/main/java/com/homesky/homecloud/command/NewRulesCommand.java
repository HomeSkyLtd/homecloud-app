package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.exceptions.NetworkException;
import com.homesky.homecloud_lib.model.Rule;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

import java.util.List;

public class NewRulesCommand implements Command{
    List<Rule> mRules;

    public NewRulesCommand(List<Rule> rules) {
        mRules = rules;
    }

    @Override
    public SimpleResponse execute() throws NetworkException {
        return HomecloudHolder.getInstance().newRules(mRules);
    }
}
