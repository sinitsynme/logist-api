package ru.sinitsynme.logistapi.exception;

import org.springframework.http.HttpStatus;

public class HttpServiceException extends RuntimeException implements ServiceException {

    private HttpStatus httpStatus;
    private String code;
    private ExceptionSeverity exceptionSeverity;


    public HttpServiceException() {
    }

    public HttpServiceException(String message,
                                Throwable cause,
                                HttpStatus httpStatus,
                                String code,
                                ExceptionSeverity exceptionSeverity) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.code = code;
        this.exceptionSeverity = exceptionSeverity;
    }

    @Override
    public ExceptionSeverity getExceptionSeverity() {
        return exceptionSeverity;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
