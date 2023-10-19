package com.resort.platform.backnode.foodtracker.model;

import com.resort.platform.backnode.foodtracker.model.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MealReservation {
    @Id
    private String id;
    private String employeeNumber;
    private MealType mealType;
    private LocalDate reservationDate;
}
