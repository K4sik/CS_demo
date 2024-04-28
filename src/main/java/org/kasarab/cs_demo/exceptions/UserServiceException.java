package org.kasarab.cs_demo.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus;

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
