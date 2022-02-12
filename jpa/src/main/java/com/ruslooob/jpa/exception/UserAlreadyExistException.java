package com.ruslooob.jpa.exception;

import com.ruslooob.jpa.model.User;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(User user) {
        super(String.format("User %s already exists", user));
    }

}
