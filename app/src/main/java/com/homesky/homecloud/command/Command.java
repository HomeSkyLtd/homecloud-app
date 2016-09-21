package com.homesky.homecloud.command;

import com.homesky.homecloud_lib.Homecloud;
import com.homesky.homecloud_lib.model.response.SimpleResponse;

public interface Command {
    public SimpleResponse execute() throws Homecloud.NetworkException;
}
