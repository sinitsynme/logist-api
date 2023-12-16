package ru.sinitsynme.logistapi.exception.service;

public class IllegalFileUploadException extends RuntimeException {

    public IllegalFileUploadException() {
    }

    public IllegalFileUploadException(String message) {
        super(message);
    }

    public IllegalFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
