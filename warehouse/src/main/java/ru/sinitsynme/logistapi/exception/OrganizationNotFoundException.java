package ru.sinitsynme.logistapi.exception;

import org.springframework.http.HttpStatus;

public class OrganizationNotFoundException extends HttpServiceException {

    public OrganizationNotFoundException() {
    }

    public OrganizationNotFoundException(String message, Throwable cause, HttpStatus httpStatus, String code, ExceptionSeverity exceptionSeverity) {
        super(message, cause, httpStatus, code, exceptionSeverity);
    }
}
