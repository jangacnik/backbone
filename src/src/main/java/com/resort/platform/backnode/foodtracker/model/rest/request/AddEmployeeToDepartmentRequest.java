package com.resort.platform.backnode.foodtracker.model.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddEmployeeToDepartmentRequest {

  @NotBlank
  String department;
  @NotBlank
  String employeeNumber;
}
