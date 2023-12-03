package com.resort.platform.backnode.foodtracker.model;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("reservations")
@AllArgsConstructor
public class MonthlyMealReservations {

  @Id
  private String id;
  @NotNull
  private List<MealReservation> mealReservations;

  public MonthlyMealReservations() {
    mealReservations = new ArrayList<>();
  }
}
