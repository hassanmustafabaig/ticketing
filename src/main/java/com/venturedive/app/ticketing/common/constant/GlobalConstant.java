package com.venturedive.app.ticketing.common.constant;

public class GlobalConstant {
    private GlobalConstant() {
    }
    // Keycloak config
    public static final String AUTH_SERVER_URL = "http://localhost:8080/auth";
    public static final String AUTH_SERVER_REALM = "local-master-realm";
    public static final String CLIENT_ID = "ticketing-be-application";
    public static final String CLIENT_SECRET = "PR9z9Zy5DmtqJwK2cIWa60bp4oT2zrhi";
    public static final String CONSOLE_USERNAME = "admin";
    public static final String CONSOLE_PASSWORD = "admin123$";

    // user roles
    public static final String ROLE_ORDER_MANAGER = "Order_Manager";
    public static final String ROLE_TICKETING_AGENT = "Ticketing_Agent";

    // APIs
    public static final String API_PREFIX_AUTH = "/auth";
    public static final String API_AUTH_LOGIN = "/login";
    public static final String API_AUTH_GET_REFRESH_TOKEN = "/get-refresh-token";
    public static final String API_AUTH_LOGOUT = "/logout";

    public static final String API_PREFIX_DELIVERY = "/delivery";
    public static final String API_DELIVERY_SEARCH = "/search";
    public static final String API_DELIVERY_GET_ALL = "/get-all";
    public static final String API_DELIVERY_ADD = "/add";


    public static final String API_PREFIX_TICKET = "/ticket";
    public static final String API_TICKET_GET_ALL = "/get-all";

    public static final String API_PREFIX_USER = "/user";
    public static final String API_USER_CREATE_ORDER_MANAGER_USER = "/create-order-manager";
    public static final String API_USER_CREATE_TICKET_AGENT_USER = "/create-ticket-agent";

}
