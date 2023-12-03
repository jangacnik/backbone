package com.resort.platform.backnode.foodtracker.model.rest.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealReportModel {

  String employeeNumber;
  String name;
  List<String> department;
  int mealCountUsed;
  int mealCountReserved;
  double mealTotalPrice;
}
