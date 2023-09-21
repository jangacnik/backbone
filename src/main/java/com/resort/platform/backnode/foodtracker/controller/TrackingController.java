package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.model.rest.request.AddTrackingEntryRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackingResponse;
import com.resort.platform.backnode.foodtracker.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/track")
public class TrackingController {
    @Autowired
    TrackingService trackingService;


    @PostMapping
    public ResponseEntity<Void> addMealEntry(@RequestBody AddTrackingEntryRequest addTrackingEntryRequest) {
        trackingService.addMealEntry(addTrackingEntryRequest.getEmployeeNumber());
        return ResponseEntity.ok(null);
    }

    @GetMapping("/current/month")
    public ResponseEntity<FoodTrackingResponse> getCurrentMonthTracking() {
        FoodTrackingResponse foodTrackingResponse = trackingService.getCurrentMonthTracking();
        return ResponseEntity.ok(foodTrackingResponse);
    }
}
