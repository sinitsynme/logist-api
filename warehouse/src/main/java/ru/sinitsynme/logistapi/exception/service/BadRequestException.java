package ru.sinitsynme.logistapi.exception.service;

import org.springframework.http.HttpStatus;
import ru.sinitsynme.logistapi.exception.ExceptionSeverity;
import ru.sinitsynme.logistapi.exception.HttpServiceException;

public class BadRequestException extends HttpServiceException {

    public BadRequestException() {
    }

    public BadRequestException(String message, Throwable cause, HttpStatus httpStatus, String code, ExceptionSeverity exceptionSeverity) {
        super(message, cause, httpStatus, code, exceptionSeverity);
    }
}
