package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;

/**
 * Created by fabio on 02/09/2016.
 */
public class NewAdminCommand implements Command {
    private String mUsername;
    private String mPassword;

    public NewAdminCommand(String username, String password) {
        mUsername = username;
        mPassword = password;
    }
    @Override
    public String execute() {
        return HomecloudHolder.getInstance().newAdmin(mUsername, mPassword);
    }
}
