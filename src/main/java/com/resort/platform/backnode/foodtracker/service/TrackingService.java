package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.auth.service.JwtService;
import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import com.resort.platform.backnode.foodtracker.model.MealEntry;
import com.resort.platform.backnode.foodtracker.model.MealPrice;
import com.resort.platform.backnode.foodtracker.model.MealReservation;
import com.resort.platform.backnode.foodtracker.model.MealTracking;
import com.resort.platform.backnode.foodtracker.model.MonthlyMealReservations;
import com.resort.platform.backnode.foodtracker.model.rest.MealEntryWithUser;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import com.resort.platform.backnode.foodtracker.model.rest.response.MealReportModel;
import com.resort.platform.backnode.foodtracker.repo.MealPricerepository;
import com.resort.platform.backnode.foodtracker.repo.MealReservationRepository;
import com.resort.platform.backnode.foodtracker.repo.TrackingRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@EnableAutoConfiguration
public class TrackingService {

  TrackingRepository trackingRepository;
  MealReservationRepository mealReservationRepository;
  JwtService jwtService;

  MealPricerepository mealPricerepository;

  FoodTrackerUserService foodTrackerUserService;


  /**
   * Adds a meal entry with the current time for the given employee with @param employeeNumber
   *
   * @param employeeNumber - employee number of the user
   */
  public void addMealEntry(String employeeNumber) {
    Calendar calendar = Calendar.getInstance();
    MealTracking mealTracking = new MealTracking();
    mealTracking.setTrackingEntries(new HashMap<>());
    String trackingId =
        "tracking_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR);
    Optional<MealTracking> mealTrackingOptional = trackingRepository.findById(trackingId);
    if (mealTrackingOptional.isPresent()) {
      mealTracking = mealTrackingOptional.get();
      addMealToEmployee(employeeNumber, mealTracking);
    } else {
      mealTracking.setId(trackingId);
      addMealToEmployee(employeeNumber, mealTracking);
    }
    trackingRepository.save(mealTracking);
  }

  /**
   * Get all meals of this month
   *
   * @return list of all recorded meals for the current month
   */
  public List<MealReportModel> getCurrentMonthTracking() {
    Calendar calendar = Calendar.getInstance();
    MealPrice mealPrice = getMealPrice();
    String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    String trackingId =
        "tracking_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR);
    String reservationId = calendar.get(Calendar.YEAR) + "-" + month.toUpperCase() + "_reservation";
    MealTracking mealTracking = trackingRepository.findById(trackingId)
        .orElseThrow(() -> new InvalidRequestException("Tracking does not exist yes"));
    Optional<MonthlyMealReservations> monthlyMealReservationsOptional = mealReservationRepository.getMonthlyMealReservationsById(
        reservationId);
    MonthlyMealReservations monthlyMealReservations = new MonthlyMealReservations();
    if (monthlyMealReservationsOptional.isPresent()) {
      monthlyMealReservations = monthlyMealReservationsOptional.get();
    }
    List<MealReportModel> mealReport = new ArrayList<>();
    for (Map.Entry<String, MealEntry> entry : mealTracking.getTrackingEntries().entrySet()) {
      FoodTrackerUserWithDepartment foodTrackerUserWithDepartment = foodTrackerUserService.getFoodTrackerUser(
          entry.getKey());
      MealReportModel mealReportModel = new MealReportModel();
      mealReportModel.setDepartment(foodTrackerUserWithDepartment.getDepartments());
      mealReportModel.setName(foodTrackerUserWithDepartment.getFirstName() + " "
          + foodTrackerUserWithDepartment.getLastName());
      mealReportModel.setMealCountUsed(entry.getValue().getMealCount());
      mealReportModel.setEmployeeNumber(foodTrackerUserWithDepartment.getEmployeeNumber());
      AtomicInteger reservedMealsCount = new AtomicInteger();
      List<MealReservation> reservedMeals = monthlyMealReservations.getMealReservations().stream()
          .filter(mealReservation ->
              mealReservation.getEmployeeNumber()
                  .equals(foodTrackerUserWithDepartment.getEmployeeNumber())).toList();
      reservedMeals.forEach(
          mealReservation -> reservedMealsCount.addAndGet(mealReservation.getMealType().size()));
      mealReportModel.setMealCountReserved(reservedMealsCount.get());
      mealReportModel.setMealTotalPrice(mealPrice.getPrice() * mealReportModel.getMealCountUsed());
      mealReport.add(mealReportModel);
    }
    return mealReport;
  }

  /**
   * Add meal to employee by employeeNumber
   *
   * @param employeeNumber - employeeNumber of employee
   * @param tracking       - meal record
   */
  private void addMealToEmployee(String employeeNumber, MealTracking tracking) {
    // add new meal entry for existing employee in tracking
    if (tracking.getTrackingEntries().containsKey(employeeNumber)) {
      MealEntry mealEntry = tracking.getTrackingEntries().get(employeeNumber);
      mealEntry.getMeals().put(mealEntry.getMealCount() + 1, LocalDateTime.now());
      mealEntry.setMealCount(mealEntry.getMealCount() + 1);
      tracking.getTrackingEntries().put(employeeNumber, mealEntry);
      return;
    }
    // add entry for employee if none exist in document
    Map<Integer, LocalDateTime> meals = new HashMap<>();
    meals.put(1, LocalDateTime.now());
    MealEntry mealEntry = new MealEntry(1, meals);
    tracking.getTrackingEntries().put(employeeNumber, mealEntry);
  }

  /**
   * Get all recorded meals for given employee by employeeNumber or E-mail
   *
   * @param emailOrEmployeeNumber - email or employeeNumber of the employee
   * @return returns all meals of the employee for this month
   */
  public MealEntryWithUser getTrackingForCurrentUser(String emailOrEmployeeNumber) {

    Calendar calendar = Calendar.getInstance();
    String trackingId =
        "tracking_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.YEAR);
    MealTracking mealTracking = trackingRepository.findById(trackingId)
        .orElseThrow(() -> new InvalidRequestException("Tracking does not exist yes"));
    FoodTrackerUserWithDepartment foodTrackerUserWithDepartment = foodTrackerUserService.getFoodTrackerUser(
        emailOrEmployeeNumber);
    return new MealEntryWithUser(foodTrackerUserWithDepartment,
        mealTracking.getTrackingEntries().get(foodTrackerUserWithDepartment.getEmployeeNumber()));
  }

  /**
   * Get price of a meal
   *
   * @return current price of a meal
   */
  public MealPrice getMealPrice() {
    return mealPricerepository.findFirstByOrderByLastUpdatedDesc()
        .orElseThrow(() -> new RuntimeException("No price found"));
  }

  /**
   * Set the meal price of a meal
   *
   * @param mealPrice new price of the meal
   */
  public void setNewMealPrice(MealPrice mealPrice) {
    mealPricerepository.save(mealPrice);
  }
}
