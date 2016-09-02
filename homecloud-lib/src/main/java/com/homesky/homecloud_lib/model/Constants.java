package com.homesky.homecloud_lib.model;

public class Constants {
    public static class Fields {
        public static class Common {
            public static final String FUNCTION = "function";
            public static final String PAYLOAD = "payload";
            public static final String STATUS = "status";
            public static final String ERROR_MESSAGE = "errorMessage";
        }

        public static class Login{
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String TOKEN = "token";
        }

        public static class Logout{
            public static final String TOKEN = "token";
        }

        public static class NewAgent{
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
        }

        public static class RegisterController{
            public static final String CONTROLLER_ID = "controllerId";
        }

        public static class GetHouseState{
            public static final String STATE = "state";
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String DATA = "data";
            public static final String COMMAND = "command";
        }
    }

    public static class Values {
        public static class Functions {
            public static final String LOGIN = "login";
            public static final String LOGOUT = "logout";
            public static final String NEW_USER = "newUser";
            public static final String NEW_ADMIN = "newAdmin";
            public static final String REGISTER_CONTROLLER = "registerController";
            public static final String GET_HOUSE_STATE = "getHouseState";
        }
    }
}
