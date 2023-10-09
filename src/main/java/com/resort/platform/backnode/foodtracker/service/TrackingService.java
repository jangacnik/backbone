package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.auth.service.JwtService;
import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import com.resort.platform.backnode.foodtracker.model.MealEntry;
import com.resort.platform.backnode.foodtracker.model.MealPrice;
import com.resort.platform.backnode.foodtracker.model.MealTracking;
import com.resort.platform.backnode.foodtracker.model.rest.MealEntryWithUser;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackingResponse;
import com.resort.platform.backnode.foodtracker.repo.MealPricerepository;
import com.resort.platform.backnode.foodtracker.repo.TrackingRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@EnableAutoConfiguration
public class TrackingService {
    TrackingRepository trackingRepository;
    JwtService jwtService;

    MealPricerepository mealPricerepository;

    FoodTrackerUserService foodTrackerUserService;

    public void addMealEntry(String employeeId) {
        Calendar calendar = Calendar.getInstance();
        MealTracking mealTracking = new MealTracking();
        mealTracking.setTrackingEntries(new HashMap<>());
        String trackingId = "tracking_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR);
        Optional<MealTracking> mealTrackingOptional = trackingRepository.findById(trackingId);
        if (mealTrackingOptional.isPresent()) {
            mealTracking = mealTrackingOptional.get();
            addMealToEmployee(employeeId, mealTracking);
        } else {
            mealTracking.setId(trackingId);
            addMealToEmployee(employeeId, mealTracking);
        }
        trackingRepository.save(mealTracking);
    }

    public FoodTrackingResponse getCurrentMonthTracking() {
        Calendar calendar = Calendar.getInstance();
        String trackingId = "tracking_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR);
        MealTracking mealTracking = trackingRepository.findById(trackingId).orElseThrow(() -> new InvalidRequestException("Tracking does not exist yes"));
        FoodTrackingResponse foodTrackingResponse = new FoodTrackingResponse(trackingId, new HashMap<>());
        for (Map.Entry<String, MealEntry> entry : mealTracking.getTrackingEntries().entrySet()) {
           FoodTrackerUserWithDepartment foodTrackerUserWithDepartment = foodTrackerUserService.getFoodTrackerUser(entry.getKey());
           foodTrackingResponse.getEntries().put(foodTrackerUserWithDepartment.getEmployeeNumber(), new MealEntryWithUser( foodTrackerUserWithDepartment, entry.getValue()));
        }
        return foodTrackingResponse;
    }

    private void addMealToEmployee(String employeeId, MealTracking tracking) {
        // add new meal entry for existing employee in tracking
        if (tracking.getTrackingEntries().containsKey(employeeId)) {
            MealEntry mealEntry = tracking.getTrackingEntries().get(employeeId);
            mealEntry.getMeals().put(mealEntry.getMealCount()+1, LocalDateTime.now());
            mealEntry.setMealCount(mealEntry.getMealCount()+1);
            tracking.getTrackingEntries().put(employeeId, mealEntry);
            return;
        }
        // add entry for employee if none exist in document
        Map<Integer, LocalDateTime> meals = new HashMap<>();
        meals.put(1, LocalDateTime.now());
        MealEntry mealEntry = new MealEntry(1, meals);
        tracking.getTrackingEntries().put(employeeId, mealEntry);
    }

    public MealEntryWithUser getTrackingForCurrentUser(String id) {
        FoodTrackingResponse temp = getCurrentMonthTracking();
        return temp.getEntries().get(id);
    }

    public MealPrice getMealPrice() {
        return mealPricerepository.findFirstByOrderByLastUpdatedDesc().orElseThrow(() -> new RuntimeException("No price found"));
    }
    public void setNewMealPrice(MealPrice mealPrice) {
        mealPricerepository.save(mealPrice);
    }
}
