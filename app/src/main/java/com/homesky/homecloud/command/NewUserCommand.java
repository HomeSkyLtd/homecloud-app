package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;

public class NewUserCommand implements Command {
    private String mUsername;
    private String mPassword;

    public NewUserCommand(String username, String password) {
        mUsername = username;
        mPassword = password;
    }
    @Override
    public String execute() {
        return HomecloudHolder.getInstance().newUser(mUsername, mPassword);
    }
}
