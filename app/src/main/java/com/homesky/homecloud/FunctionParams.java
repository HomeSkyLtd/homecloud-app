package com.homesky.homecloud;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionParams {
    public static final Map<String, List<String>> functionParams;

    static {
        functionParams = new HashMap<>();
        functionParams.put("Login", Arrays.asList("username", "password"));
        functionParams.put("Logout", new ArrayList<String>());
        functionParams.put("New admin", Arrays.asList("username", "password"));
        functionParams.put("Register controller", Arrays.asList("username", "password", "controllerId"));
        functionParams.put("Get house state", Arrays.asList("username", "password"));
        functionParams.put("New action", Arrays.asList("username", "password", "nodeId", "controllerId", "commandId", "value"));
        functionParams.put("Accept node", Arrays.asList("username", "password", "nodeId", "controllerId"));
    }
}
