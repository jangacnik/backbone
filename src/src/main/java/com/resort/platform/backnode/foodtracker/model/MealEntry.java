package com.resort.platform.backnode.foodtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class MealEntry {
    int mealCount;
    Map<Integer, LocalDateTime> meals;
}
