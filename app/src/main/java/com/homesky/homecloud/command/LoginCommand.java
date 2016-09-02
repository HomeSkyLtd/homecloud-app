package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;

public class LoginCommand implements Command{

    @Override
    public String execute() {
        return HomecloudHolder.getInstance().login();
    }
}
