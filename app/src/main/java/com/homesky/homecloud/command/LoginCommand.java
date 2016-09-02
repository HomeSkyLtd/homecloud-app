package com.homesky.homecloud.command;

import com.homesky.homecloud.HomecloudHolder;

public class LoginCommand implements Command{
//    private String username;
//    private String password;
//    private String token;
//
//    public LoginCommand(String username, String password, String token) {
//        this.username = username;
//        this.password = password;
//        this.token = token;
//    }

    @Override
    public String execute() {
        return HomecloudHolder.getInstance().login();
    }
}
