package com.resort.platform.backnode.foodtracker.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document("departments")
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
}
