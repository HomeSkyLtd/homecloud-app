package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;

public class RegisterControllerCommand implements Command {
    private String mControllerId;

    public RegisterControllerCommand(String controllerId){
        mControllerId = controllerId;
    }

    @Override
    public String execute() {
        return HomecloudHolder.getInstance().registerController(mControllerId);
    }
}
