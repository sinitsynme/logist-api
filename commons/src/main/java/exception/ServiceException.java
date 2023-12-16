package exception;

import org.springframework.http.HttpStatus;

public interface ServiceException {

    ExceptionSeverity getExceptionSeverity();
    String getCode();
    HttpStatus getHttpStatus();
}
