package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.exceptions.NetworkException;
import com.homesky.homecloud_lib.model.Rule;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

/**
 * Created by fabio on 19/10/2016.
 */

public class RemoveRuleCommand implements Command {

    private Rule mRule;

    public RemoveRuleCommand(Rule rule) {
        mRule = rule;
    }

    @Override
    public SimpleResponse execute() throws NetworkException {
        return HomecloudHolder.getInstance().removeRule(mRule);
    }
}
