package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.auth.service.JwtService;
import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import com.resort.platform.backnode.foodtracker.model.*;
import com.resort.platform.backnode.foodtracker.model.rest.MealEntryWithUser;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackingResponse;
import com.resort.platform.backnode.foodtracker.model.rest.response.MealReportModel;
import com.resort.platform.backnode.foodtracker.repo.MealPricerepository;
import com.resort.platform.backnode.foodtracker.repo.MealReservationRepository;
import com.resort.platform.backnode.foodtracker.repo.TrackingRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
@EnableAutoConfiguration
public class TrackingService {
    TrackingRepository trackingRepository;
    MealReservationRepository mealReservationRepository;
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

    public List<MealReportModel> getCurrentMonthTracking() {
        Calendar calendar = Calendar.getInstance();
        MealPrice mealPrice = getMealPrice();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String trackingId = "tracking_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR);
        String reservationId = calendar.get(Calendar.YEAR) + "-" + month.toUpperCase()+"_reservation";
        MealTracking mealTracking = trackingRepository.findById(trackingId).orElseThrow(() -> new InvalidRequestException("Tracking does not exist yes"));
        MonthlyMealReservations monthlyMealReservations = mealReservationRepository.getMonthlyMealReservationsById( reservationId).orElseThrow();
        List<MealReportModel> mealReport = new ArrayList<>();
        for (Map.Entry<String, MealEntry> entry : mealTracking.getTrackingEntries().entrySet()) {
           FoodTrackerUserWithDepartment foodTrackerUserWithDepartment = foodTrackerUserService.getFoodTrackerUser(entry.getKey());
           MealReportModel mealReportModel = new MealReportModel();
           mealReportModel.setDepartment(foodTrackerUserWithDepartment.getDepartments());
           mealReportModel.setName(foodTrackerUserWithDepartment.getFirstName() + " " + foodTrackerUserWithDepartment.getLastName());
           mealReportModel.setMealCountUsed(entry.getValue().getMealCount());
           mealReportModel.setEmployeeNumber(foodTrackerUserWithDepartment.getEmployeeNumber());
           AtomicInteger reservedMealsCount = new AtomicInteger();
           List<MealReservation> reservedMeals = monthlyMealReservations.getMealReservations().stream().filter(mealReservation ->
                   mealReservation.getEmployeeNumber().equals(foodTrackerUserWithDepartment.getEmployeeNumber())).toList();
           reservedMeals.forEach( mealReservation -> reservedMealsCount.addAndGet(mealReservation.getMealType().size()));
           mealReportModel.setMealCountReserved(reservedMealsCount.get());
           mealReportModel.setMealTotalPrice(mealPrice.getPrice()*mealReportModel.getMealCountUsed());
           mealReport.add(mealReportModel);
        }
        return mealReport;
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

        Calendar calendar = Calendar.getInstance();
        String trackingId = "tracking_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR);
        MealTracking mealTracking = trackingRepository.findById(trackingId).orElseThrow(() -> new InvalidRequestException("Tracking does not exist yes"));
        FoodTrackerUserWithDepartment foodTrackerUserWithDepartment = foodTrackerUserService.getFoodTrackerUser(id);
        return new MealEntryWithUser(foodTrackerUserWithDepartment, mealTracking.getTrackingEntries().get(foodTrackerUserWithDepartment.getEmployeeNumber()));
    }

    public MealPrice getMealPrice() {
        return mealPricerepository.findFirstByOrderByLastUpdatedDesc().orElseThrow(() -> new RuntimeException("No price found"));
    }
    public void setNewMealPrice(MealPrice mealPrice) {
        mealPricerepository.save(mealPrice);
    }
}
