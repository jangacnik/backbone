package com.resort.platform.backnode.foodtracker.model.rest.request;

import lombok.Data;

@Data
public class DepartmentDeletionRequest {

  String employeeNumber;
  String departmentName;
  String id;
}
