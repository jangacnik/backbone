package com.resort.platform.backnode.foodtracker.model.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class FoodTrackerUserWithDepartment extends FoodTrackerUser {
    List<String> departments;

    @Builder
    public FoodTrackerUserWithDepartment(String lastName, String firstName, String email, String employeeNumber, List<String> departments) {
        super(lastName, firstName, email, employeeNumber);
        this.departments = departments;
    }
}
