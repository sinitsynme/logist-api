package ru.sinitsynme.logistapi.exception;

public class ServiceExceptionMessageTemplates {

    public static final String AUTHORITY_NOT_FOUND_TEMPLATE
            = "Authority with name %s is not found";

    public static final String AUTHORITY_EXISTS_TEMPLATE
            = "Authority with name %s already exists";

    public static final String USER_NOT_FOUND_TEMPLATE
            = "User with email %s is not found";

    public static final String USER_EXISTS_TEMPLATE
            = "User with email %s already exists";

    public static final String INVALID_AUTH_TEMPLATE
            = "Authentication for user with email '%s' was invalid due to cause '%s'";
}
