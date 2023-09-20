package com.resort.platform.backnode.foodtracker.model.rest.response;

import com.resort.platform.backnode.foodtracker.model.rest.MealEntryWithUser;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
public class FoodTrackingResponse {
    String trackingId;
    Map<String, MealEntryWithUser> entries;
}
