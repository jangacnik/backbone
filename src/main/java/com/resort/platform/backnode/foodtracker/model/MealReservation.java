package com.resort.platform.backnode.foodtracker.model;

import com.resort.platform.backnode.foodtracker.model.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class MealReservation {
    @Id
    private String id;
    private String employeeNumber;
    private String name;
    private List<String> departments;
    private List<MealType> mealType;
    private LocalDate reservationDate;
    private LocalDateTime reservationTime;
}
