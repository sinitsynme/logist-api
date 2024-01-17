package ru.sinitsynme.logistapi.exception;

import exception.ExceptionSeverity;
import exception.HttpServiceException;
import org.springframework.http.HttpStatus;

public class OtpIsNotFoundOrExpiredException extends HttpServiceException {

    public OtpIsNotFoundOrExpiredException(String message,
                                           Throwable cause,
                                           HttpStatus httpStatus,
                                           String code,
                                           ExceptionSeverity exceptionSeverity) {
        super(message, cause, httpStatus, code, exceptionSeverity);
    }
}
