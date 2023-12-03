package com.resort.platform.backnode.foodtracker.model;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MealEntry {

  int mealCount;
  Map<Integer, LocalDateTime> meals;
}
