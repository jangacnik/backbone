package com.resort.platform.backnode.foodtracker.model.planday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EmployeeSubModel {
    //    ArrayList<String> securityGroups;
    int id; //employeenumber
    String lastName;
    String firstName;
    String email;
    ArrayList<String> departments;
}
