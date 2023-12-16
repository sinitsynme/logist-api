package ru.sinitsynme.logistapi.exception.service;

public class GetFileFromRootException extends RuntimeException {

    public GetFileFromRootException() {
    }

    public GetFileFromRootException(String message) {
        super(message);
    }

    public GetFileFromRootException(String message, Throwable cause) {
        super(message, cause);
    }
}
