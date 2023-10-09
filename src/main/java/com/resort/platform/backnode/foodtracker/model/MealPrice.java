package com.resort.platform.backnode.foodtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Document("meal_price")
public class MealPrice {
    @Id
    private String id;
    private double price;
    private LocalDateTime lastUpdated;
}
