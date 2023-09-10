package com.resort.platform.backnode.foodtracker.model.rest.request;

import lombok.Data;

@Data
public class FoodTrackerUserRequest {
    private String username;
    private String employeeNumber;
}
