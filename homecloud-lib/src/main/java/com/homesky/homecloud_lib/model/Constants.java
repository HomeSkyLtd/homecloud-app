package com.homesky.homecloud_lib.model;

public class Constants {
    public static class Fields {
        public static class Common {
            public static final String FUNCTION = "function";
            public static final String PAYLOAD = "payload";
            public static final String STATUS = "status";
            public static final String ERROR_MESSAGE = "errorMessage";
            public static final String NOTIFICATION = "notification";

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

        public static class NewAction{
            public static final String ACTION = "action";
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String COMMAND_ID = "commandId";
            public static final String VALUE = "value";
        }

        public static class NewRules{
            public static final String COMMAND = "command";
            public static final String RULES = "rules";
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String COMMAND_ID = "commandId";
            public static final String VALUE = "value";
            public static final String CLAUSES = "clauses";
            public static final String LHS = "lhs";
            public static final String OPERATOR = "operator";
            public static final String RHS = "rhs";
        }

        public static class AcceptRule{
            public static final String COMMAND = "command";
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String COMMAND_ID = "commandId";
            public static final String VALUE = "value";
        }

        public static class RemoveRule{
            public static final String COMMAND = "command";
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String COMMAND_ID = "commandId";
            public static final String VALUE = "value";
        }

        public static class SetNodeExtra{
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String EXTRA = "extra";
        }

        public static class RuleResponse{
            public static final String COMMAND = "command";
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String COMMAND_ID = "commandId";
            public static final String VALUE = "value";
            public static final String CLAUSES = "clauses";
            public static final String RULES = "rules";
            public static final String LHS = "lhs";
            public static final String OPERATOR = "operator";
            public static final String RHS = "rhs";

        }

        public static class NodesResponse{
            public static final String NODES = "nodes";
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String NODE_CLASS = "nodeClass";
            public static final String ACCEPTED = "accepted";
            public static final String ALIVE = "alive";
            public static final String EXTRA = "extra";
            public static final String DATA_TYPE = "dataType";
            public static final String COMMAND_TYPE = "commandType";

            public static final String ID = "id";
            public static final String MEASURE_STRATEGY = "measureStrategy";
            public static final String TYPE = "type";
            public static final String RANGE = "range";
            public static final String DATA_CATEGORY = "dataCategory";
            public static final String UNIT = "unit";
            public static final String COMMAND_CATEGORY = "commandCategory";
        }

        public static class AcceptNode{
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String ACCEPT = "accept";
        }

        public static class RemoveNode{
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
        }

        public static class ActionResult{
            public static final String ACTION = "action";
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String VALUE = "value";
            public static final String RESULT = "result";
            public static final String COMMAND_ID = "commandId";
        }

        public static class LearntRules{
            public static final String QUANTITY = "quantity";
        }

        public static class DetectedNode{
            public static final String QUANTITY = "quantity";
        }

        public static class ConflictingRulesResponse{
            public static final String CONFLICTING_RULE = "conflictingRule";
            public static final String COMMAND = "command";
            public static final String NODE_ID = "nodeId";
            public static final String CONTROLLER_ID = "controllerId";
            public static final String COMMAND_ID = "commandId";
            public static final String VALUE = "value";
            public static final String CLAUSES = "clauses";
            public static final String LHS = "lhs";
            public static final String OPERATOR = "operator";
            public static final String RHS = "rhs";
        }

        public static class GetControllers{
            public static final String CONTROLLERS = "controllers";
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
            public static final String NEW_ACTION = "newAction";
            public static final String NEW_RULES = "newRules";
            public static final String GET_RULES = "getRules";
            public static final String GET_LEARNT_RULES = "getLearntRules";
            public static final String SET_NODE_EXTRA = "setNodeExtra";
            public static final String GET_NODES_INFO = "getNodesInfo";
            public static final String ACCEPT_NODE = "acceptNode";
            public static final String REMOVE_NODE = "removeNode";
            public static final String ACCEPT_RULE = "acceptRule";
            public static final String REMOVE_RULE = "removeRule";
            public static final String GET_CONTROLLERS = "getControllers";
        }

        public static class Notifications {
            public static final String ACTION_RESULT = "actionResult";
            public static final String LEARNT_RULES = "newRules";
            public static final String DETECTED_NODE = "detectedNode";
        }
    }
}
