package com.venturedive.app.ticketing.errorhandling;

public enum ErrorTypes {

    INTERNAL_SERVER_ERROR("TKT-0001", "Something wrong happened with the application"),
    EMAIL_ALREADY_EXIST("TKT-0002", "Email already exist"),
    UNABLE_TO_REGISTER_USER("TKT-0003", "Unable to register user"),
    USER_DOES_NOT_EXIST("TKT-0004", "User does not exist"),
    INVALID_EMAIL_OR_PASSWORD("TKT-0005", "Invalid email or password"),
	INVALID_INPUT_DATA("TKT-0006", "Invalid input data"),
	NO_RESULT_FOUND("TKT-0007", "No result found"),
	USER_ALREADY_EXIST("TKT-0008", "User already exist"),
    AUTHENTICATION_FALIURE("TKT-0009", "Authentication Failed. Username or Password not valid"),
    USERNAME_FETCHING_FROM_TOKEN("TKT-0010", "An error occurred while fetching Username from Token"),
    EXPIRED_TOKEN("TKT-0011", "The token has expired"),
    ACCESS_DENIED("TKT-0012", "You are not authorized"),
    INVALID_TOKEN("TKT-0013", "Invalid token"),
    RUNTIME_ERROR("TKT-0014", "Runtime error occurred"),
    ILLEGAL_STATE("TKT-0015", "Something wrong happened with the application. Illegal state");

    private final String code;
    private final String message;

    ErrorTypes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
