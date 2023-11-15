package com.resort.platform.backnode.foodtracker.model;

import com.resort.platform.backnode.foodtracker.model.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class MealReservation {
    @Id
    private String id;
    private String employeeNumber;
    private List<MealType> mealType;
    private LocalDate reservationDate;
}
