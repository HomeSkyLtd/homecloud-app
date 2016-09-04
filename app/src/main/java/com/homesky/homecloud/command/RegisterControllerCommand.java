package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

public class RegisterControllerCommand implements Command {
    private String mControllerId;

    public RegisterControllerCommand(String controllerId){
        mControllerId = controllerId;
    }

    @Override
    public SimpleResponse execute() {
        return HomecloudHolder.getInstance().registerController(mControllerId);
    }
}