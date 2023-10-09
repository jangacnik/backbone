package com.resort.platform.backnode.foodtracker.model.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodTrackerUser {
    @Id
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String employeeNumber;

    public FoodTrackerUser(String lastName, String firstName, String email, String employeeNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.employeeNumber = employeeNumber;
    }
}
