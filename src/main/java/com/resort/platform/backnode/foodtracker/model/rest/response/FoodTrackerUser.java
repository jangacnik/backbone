package com.resort.platform.backnode.foodtracker.model.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FoodTrackerUser {
    private String lastName;
    private String firstName;
    private String email;
    private String employeeNumber;
}
