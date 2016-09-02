package com.homesky.homecloud_lib.model;

public class Constants {
    public static class Fields {
        public static class Common {
            public static final String FUNCTION = "function";
            public static final String PAYLOAD = "payload";
        }

        public static class Login{
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";
            public static final String TOKEN = "token";
        }

        public static class Logout{
            public static final String TOKEN = "token";
        }
    }

    public static class Values {
        public static class Functions {
            public static final String LOGIN = "login";
            public static final String LOGOUT = "logout";
        }
    }
}
