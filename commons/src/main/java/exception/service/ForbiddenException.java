package exception.service;

import exception.ExceptionSeverity;
import exception.HttpServiceException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpServiceException {

    public ForbiddenException() {
    }

    public ForbiddenException(String message, Throwable cause, HttpStatus httpStatus, String code, ExceptionSeverity exceptionSeverity) {
        super(message, cause, httpStatus, code, exceptionSeverity);
    }
}
