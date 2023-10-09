package com.resort.platform.backnode.foodtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class DailyMealReservations {
    @Id
    private String id;
    private LocalDate reservationDate;
    private ArrayList<MealReservation> mealReservations;
}
