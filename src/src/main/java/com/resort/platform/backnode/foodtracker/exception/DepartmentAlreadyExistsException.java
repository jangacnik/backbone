package com.resort.platform.backnode.foodtracker.exception;

public class DepartmentAlreadyExistsException extends RuntimeException{
    public DepartmentAlreadyExistsException(String message) {
        super(message);
    }
}
