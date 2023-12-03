package com.resort.platform.backnode.foodtracker.model.rest.request;

import com.resort.platform.backnode.foodtracker.model.MealReservation;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ReservationRequest {

  LocalDate localDate;
  MealReservation mealReservation;
}
