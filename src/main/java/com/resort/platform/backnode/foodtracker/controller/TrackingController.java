package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import com.resort.platform.backnode.foodtracker.model.MealPrice;
import com.resort.platform.backnode.foodtracker.model.rest.MealEntryWithUser;
import com.resort.platform.backnode.foodtracker.model.rest.request.AddTrackingEntryRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.MealReportModel;
import com.resort.platform.backnode.foodtracker.service.TrackingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/track")
public class TrackingController {

  @Autowired
  TrackingService trackingService;

  @Value("${com.resort.platform.backnode.meal.passcode}")
  private String qrPasscode;

  /**
   * Add Meal Entry to the list of monthly meals
   *
   * @param addTrackingEntryRequest - employeeNumber and QR code secret
   * @return HTTP.OK if success
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @PostMapping
  public ResponseEntity<Void> addMealEntry(
      @RequestBody AddTrackingEntryRequest addTrackingEntryRequest) {
    if (!qrPasscode.equals(addTrackingEntryRequest.getQrPasscode())) {
      throw new InvalidRequestException("Invalid passcode, QR Code is not valid");
    }
    trackingService.addMealEntry(addTrackingEntryRequest.getEmployeeNumber());
    return ResponseEntity.ok(null);
  }

  /**
   * Get all meals for current month
   *
   * @return all recorded meals for current month
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/current/month")
  public ResponseEntity<List<MealReportModel>> getCurrentMonthTracking() {
    List<MealReportModel> mealReport = trackingService.getCurrentMonthTracking();
    return ResponseEntity.ok(mealReport);
  }

  /**
   * Returns full Employee data with all of his monthly recorded meals
   *
   * @param employeeNumber - employee number
   * @return MealEntryWithUser
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/{employeeNumber}")
  public ResponseEntity<MealEntryWithUser> getCurrentFoodTrackerUser(
      @PathVariable String employeeNumber) {
    return ResponseEntity.ok(trackingService.getTrackingForCurrentUser(employeeNumber));
  }

  /**
   * Set meal price
   *
   * @param mealPrice - Price of one meal with the time of change
   * @return HTTP.OK if success
   */
  @PreAuthorize("hasAnyRole('ADMIN')")
  @PostMapping("/price")
  public ResponseEntity<Void> setNewPrice(@RequestBody MealPrice mealPrice) {
    this.trackingService.setNewMealPrice(mealPrice);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Get current meal price
   *
   * @return latest meal Price
   */
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping("/price")
  public ResponseEntity<MealPrice> getNewPrice() {
    return ResponseEntity.ok(this.trackingService.getMealPrice());
  }
}
