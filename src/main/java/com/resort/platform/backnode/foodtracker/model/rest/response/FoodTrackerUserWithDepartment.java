package com.resort.platform.backnode.foodtracker.model.rest.response;

import com.resort.platform.backnode.auth.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class FoodTrackerUserWithDepartment extends FoodTrackerUser {
    List<String> departments;
    List<Role> roles;
    String oldEmail;
    @Builder
    public FoodTrackerUserWithDepartment(String lastName, String firstName, String email, String employeeNumber, List<String> departments, List<Role> roles) {
        super(lastName, firstName, email, employeeNumber);
        this.departments = departments;
        this.roles = roles;
    }
}
