package ru.sinitsynme.logistapi.exception.service;

@Deprecated(forRemoval = true)
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
