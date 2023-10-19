package com.resort.platform.backnode.foodtracker.model.rest.request;

import com.resort.platform.backnode.foodtracker.model.MealReservation;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequest {
    LocalDate localDate;
    MealReservation mealReservation;
}
