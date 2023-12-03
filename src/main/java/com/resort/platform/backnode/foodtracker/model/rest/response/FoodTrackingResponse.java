package com.resort.platform.backnode.foodtracker.model.rest.response;

import com.resort.platform.backnode.foodtracker.model.rest.MealEntryWithUser;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
public class FoodTrackingResponse {

  String trackingId;
  Map<String, MealEntryWithUser> entries;
}
