package com.resort.platform.backnode.foodtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document("foodtracking")
@RequiredArgsConstructor
@AllArgsConstructor
public class MealTracking {
    // ID always in form tracking-{month}-{year}
    @Id
    String id;
    Map<String, MealEntry> trackingEntries;
}
