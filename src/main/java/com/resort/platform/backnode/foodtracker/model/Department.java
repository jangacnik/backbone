package com.resort.platform.backnode.foodtracker.model;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("departments_test")
@AllArgsConstructor
@RequiredArgsConstructor
public class Department {

  @Id
  private String id;
  @NotNull
  @Indexed(unique = true)
  private String departmentName;
  @NotNull
  private ArrayList<String> employees;

  public Department(String departmentName, ArrayList<String> employees) {
    this.departmentName = departmentName;
    this.employees = employees;
  }
}
