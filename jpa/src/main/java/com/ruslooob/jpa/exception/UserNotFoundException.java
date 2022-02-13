package com.ruslooob.jpa.exception;

import static java.lang.String.format;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super(format("User with id %d not found", id));
    }

}
