package com.resort.platform.backnode.foodtracker.model.planday;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EmployeeSubModel {

  //    ArrayList<String> securityGroups;
  int id; //employeenumber
  String lastName;
  String firstName;
  String email;
  String salaryIdentifier;
  ArrayList<String> departments;
}
