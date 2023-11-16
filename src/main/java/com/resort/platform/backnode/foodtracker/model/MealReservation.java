package com.resort.platform.backnode.foodtracker.model;

import com.resort.platform.backnode.foodtracker.model.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
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
}
