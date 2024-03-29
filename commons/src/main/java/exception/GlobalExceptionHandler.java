package exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String VALIDATION_FAILED_CODE = "-1";
    public static final String VALIDATION_ERROR =
            "Validation error";

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @Autowired
    public GlobalExceptionHandler(ObjectMapper objectMapper, Clock clock) {
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> notFoundException(HttpServiceException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(clock),
                ex.getMessage(), ex.getCode());

        logger.warn("Exception handled: {}", exceptionResponse.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> badRequestException(HttpServiceException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(clock),
                ex.getMessage(), ex.getCode());

        logger.warn("Exception handled: {}", exceptionResponse.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> unauthorizedException(HttpServiceException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(clock),
                ex.getMessage(), ex.getCode());

        logger.warn("Exception handled: {}", exceptionResponse.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionResponse> forbiddenException(HttpServiceException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(clock),
                ex.getMessage(), ex.getCode());

        logger.warn("Exception handled: {}", exceptionResponse.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> serverErrorException(ServerErrorException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(clock),
                ex.getMessage(), ex.getCode());

        logger.error("Server error: {}", exceptionResponse.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(clock),
                VALIDATION_ERROR,
                VALIDATION_FAILED_CODE);
        exceptionResponse.setValidationErrors(errors);

        logger.warn("Exception handled: {}", exceptionResponse.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
