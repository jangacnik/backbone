package com.resort.platform.backnode.foodtracker.model.rest;

import com.resort.platform.backnode.foodtracker.model.MealEntry;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealEntryWithUser {
    FoodTrackerUserWithDepartment user;
    MealEntry mealEntry;
}
