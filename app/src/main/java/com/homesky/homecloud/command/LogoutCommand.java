package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.Homecloud;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

public class LogoutCommand implements Command {
    @Override
    public SimpleResponse execute() throws Homecloud.NetworkException {
        return HomecloudHolder.getInstance().logout();
    }
}
