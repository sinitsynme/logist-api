package exception.service;

import exception.ExceptionSeverity;
import exception.HttpServiceException;
import org.springframework.http.HttpStatus;

public class ServerErrorException extends HttpServiceException {

    public ServerErrorException() {
    }

    public ServerErrorException(String message, Throwable cause, HttpStatus httpStatus, String code, ExceptionSeverity exceptionSeverity) {
        super(message, cause, httpStatus, code, exceptionSeverity);
    }
}