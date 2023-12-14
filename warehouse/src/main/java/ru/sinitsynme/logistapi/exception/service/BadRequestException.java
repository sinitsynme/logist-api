package ru.sinitsynme.logistapi.exception.service;

import exception.ExceptionSeverity;
import exception.HttpServiceException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpServiceException {

    public BadRequestException() {
    }

    public BadRequestException(String message, Throwable cause, HttpStatus httpStatus, String code, ExceptionSeverity exceptionSeverity) {
        super(message, cause, httpStatus, code, exceptionSeverity);
    }
}
