package com.homesky.homecloud.command;

import com.homesky.homecloud_lib.exceptions.NetworkException;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

public class LoginCommand implements Command{

    @Override
    public SimpleResponse execute() throws NetworkException {
//        return HomecloudHolder.getInstance().login();
        return null;
    }
}
