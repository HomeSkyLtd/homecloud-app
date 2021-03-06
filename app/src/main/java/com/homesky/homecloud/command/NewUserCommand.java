package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;
import com.homesky.homecloud_lib.exceptions.NetworkException;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

public class NewUserCommand implements Command {
    private String mUsername;
    private String mPassword;

    public NewUserCommand(String username, String password) {
        mUsername = username;
        mPassword = password;
    }
    @Override
    public SimpleResponse execute() throws NetworkException {
        return HomecloudHolder.getInstance().newUser(mUsername, mPassword);
    }
}
