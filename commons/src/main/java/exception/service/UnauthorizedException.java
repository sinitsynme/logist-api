package exception.service;

import exception.ExceptionSeverity;
import exception.HttpServiceException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpServiceException {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message, Throwable cause, HttpStatus httpStatus, String code, ExceptionSeverity exceptionSeverity) {
        super(message, cause, httpStatus, code, exceptionSeverity);
    }
}
