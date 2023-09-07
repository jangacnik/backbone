package com.resort.platform.backnode.foodtracker.exception;


public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
