package ru.sinitsynme.logistapi.exception;

public class ServiceExceptionCodes {
    public static final String AUTHORITY_NOT_FOUND_CODE = "-200";
    public static final String AUTHORITY_EXISTS_CODE = "-201";
    public static final String AUTHORITY_IS_BASIC_CODE = "-202";
    public static final String USERS_HAVE_AUTHORITY_THAT_IS_BEING_REMOVED_CODE = "-203";
    public static final String AUTHORITY_MUST_HAVE_ROLE_PREFIX_CODE = "-204";
    public static final String USER_NOT_FOUND_CODE = "-300";
    public static final String USER_EXISTS_CODE = "-301";
    public static final String USER_ALREADY_BLOCKED_CODE = "-302";
    public static final String USER_ALREADY_DISABLED_CODE = "-303";
    public static final String INVALID_JWT_CODE = "-400";
    public static final String FORBIDDEN_CODE = "-403";
    public static final String INVALID_AUTH_CODE = "-500";
}
