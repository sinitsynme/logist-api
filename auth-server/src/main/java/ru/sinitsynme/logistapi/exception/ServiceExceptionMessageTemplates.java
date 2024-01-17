package ru.sinitsynme.logistapi.exception;

public class ServiceExceptionMessageTemplates {
    public static final String OTP_NOT_FOUND_OR_EXPIRED_TEMPLATE
            = "OTP for user with username %s wasn't found or was expired";
    public static final String OTP_IS_WRONG_TEMPLATE
            = "OTP for user with username %s was wrong";
}
