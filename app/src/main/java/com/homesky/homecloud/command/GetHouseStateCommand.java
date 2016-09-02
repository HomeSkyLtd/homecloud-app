package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

public class GetHouseStateCommand implements Command {
    @Override
    public SimpleResponse execute() {
        return HomecloudHolder.getInstance().getHouseState();
    }
}
