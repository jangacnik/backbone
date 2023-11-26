package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import com.resort.platform.backnode.foodtracker.model.MealPrice;
import com.resort.platform.backnode.foodtracker.model.rest.MealEntryWithUser;
import com.resort.platform.backnode.foodtracker.model.rest.request.AddTrackingEntryRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.MealReportModel;
import com.resort.platform.backnode.foodtracker.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/track")
public class TrackingController {
    @Autowired
    TrackingService trackingService;

    @Value("${com.resort.platform.backnode.meal.passcode}")
    private String qrPasscode;

    @Value("#{new Double('${com.resort.platform.backnode.meal.price}')}")
    private Double price;

    @PostMapping
    public ResponseEntity<Void> addMealEntry(@RequestBody AddTrackingEntryRequest addTrackingEntryRequest) {
        if (!qrPasscode.equals(addTrackingEntryRequest.getQrPasscode())) {
            throw new InvalidRequestException("Invalid passcode, QR Code is not valid");
        }
        trackingService.addMealEntry(addTrackingEntryRequest.getEmployeeNumber());
        return ResponseEntity.ok(null);
    }

    @GetMapping("/current/month")
    public ResponseEntity<List<MealReportModel>> getCurrentMonthTracking() {
        List<MealReportModel> mealReport = trackingService.getCurrentMonthTracking();
        return ResponseEntity.ok(mealReport);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealEntryWithUser> getCurretnFoodTrackerUser(@PathVariable String id) {
        return ResponseEntity.ok(trackingService.getTrackingForCurrentUser(id));
    }

    @PostMapping("/price")
    public ResponseEntity<Void> setNewPrice(@RequestBody MealPrice mealPrice) {
        this.trackingService.setNewMealPrice(mealPrice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/price")
    public ResponseEntity<MealPrice> setNewPrice() {
        return ResponseEntity.ok(this.trackingService.getMealPrice());
    }
}
