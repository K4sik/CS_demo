package org.kasarab.cs_demo.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ServerExceptionHandler {

    private static final Logger log = LogManager.getLogger();

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleExceptions(Exception ex, WebRequest request) {
        log.error("An exception occurred: {}", ex.getMessage(), ex);
        ErrorMessage errorMessage = new ErrorMessage(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("An exception occurred: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UserServiceException.class)
    public ResponseEntity<Object> handleServerException(UserServiceException ex, WebRequest request) {
        log.error("An exception occurred: {}", ex.getMessage(), ex);
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), ex.getHttpStatus());
        log.error("An exception occurred: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, ex.getHttpStatus());
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleServerException(NoHandlerFoundException ex, WebRequest request) {
        log.error("An exception occurred: {}", ex.getMessage(), ex);
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
        log.error("An exception occurred: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        log.error("An exception occurred: {}", ex.getMessage(), ex);
        ErrorMessage errorMessage = new ErrorMessage(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        log.error("An exception occurred: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("An exception occurred: {}", ex.getMessage(), ex);
        ErrorMessage errorMessage = new ErrorMessage(ex.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        log.error("An exception occurred: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        log.error("An exception occurred: {}", ex.getMessage(), ex);
        ErrorMessage errorMessage = new ErrorMessage(ex.getConstraintViolations().iterator().next().getMessage(), HttpStatus.BAD_REQUEST);
        log.error("An exception occurred: {}", errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }



}
