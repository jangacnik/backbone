package com.resort.platform.backnode.auth.exceptions;

public class UserAlreadyExistsExceptions extends RuntimeException{
    public UserAlreadyExistsExceptions(String message) {
        super(message);
    }
}
