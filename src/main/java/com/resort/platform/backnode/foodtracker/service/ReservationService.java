package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import com.resort.platform.backnode.foodtracker.model.MonthlyMealReservations;
import com.resort.platform.backnode.foodtracker.model.MealReservation;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import com.resort.platform.backnode.foodtracker.repo.MealReservationRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.Local;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {
    private MealReservationRepository mealReservationRepository;
    private FoodTrackerUserService foodTrackerUserService;

    public List<MealReservation> getReservationsByDate(LocalDate localDate) {

        String id = localDate.getYear() + "-" + localDate.getMonth()+"_reservation";
        Optional<MonthlyMealReservations> monthlyMealReservationsOptional = mealReservationRepository.getMonthlyMealReservationsById(id);

        if(monthlyMealReservationsOptional.isPresent()) {
            MonthlyMealReservations monthlyMealReservations = monthlyMealReservationsOptional.get();
            List<MealReservation> mealReservations = monthlyMealReservations.getMealReservations().stream().filter(res -> res.getReservationDate().getMonth().equals(localDate.getMonth()) && localDate.getDayOfMonth() == res.getReservationDate().getDayOfMonth()).collect(Collectors.toList());
            for(MealReservation mealReservation: mealReservations) {
               FoodTrackerUserWithDepartment usr =  foodTrackerUserService.getFoodTrackerUser(mealReservation.getEmployeeNumber());
                mealReservation.setDepartments(usr.getDepartments());
                mealReservation.setName(usr.getFirstName() + " " + usr.getLastName());
            }
            return  mealReservations;
        }
        return Collections.emptyList();
    }

    public List<MealReservation> getReservationsOfUser(String token) {
        LocalDate date = LocalDate.now();
        String id = date.getYear() + "-" + date.getMonth()+"_reservation";
        FoodTrackerUserWithDepartment usr = foodTrackerUserService.getCurrentUser(token);
        Optional<MonthlyMealReservations> monthlyMealReservationsOptional = mealReservationRepository.getMonthlyMealReservationsById(id);

        if(monthlyMealReservationsOptional.isPresent()) {
            MonthlyMealReservations monthlyMealReservations = monthlyMealReservationsOptional.get();
            List<MealReservation> mealReservations = monthlyMealReservations.getMealReservations().stream().filter(res -> res.getReservationDate().getMonth().equals(date.getMonth()) && res.getEmployeeNumber().equals(usr.getEmployeeNumber())).collect(Collectors.toList());
            return  mealReservations;
        }
        return Collections.emptyList();
    }

    public void addReservationForNextDay(MealReservation mealReservation) {
        LocalDate localDate = LocalDate.now().plusDays(1);
        mealReservation.setId(mealReservation.getEmployeeNumber()+ "_" + localDate.toString());
        mealReservation.setReservationTime(LocalDateTime.now());
        mealReservation.setReservationDate(localDate);
        String id = mealReservation.getReservationDate().getYear() + "-" + mealReservation.getReservationDate().getMonth()+"_reservation";
        Optional<MonthlyMealReservations> dailyMealReservationsOptional = mealReservationRepository.getMonthlyMealReservationsById(id);
        if (dailyMealReservationsOptional.isPresent()) {
            MonthlyMealReservations monthlyMealReservations = dailyMealReservationsOptional.get();
            Optional<MealReservation> mealReservationOptional = monthlyMealReservations.getMealReservations().stream().filter(mealReservation2 -> mealReservation2.getId().equals(mealReservation.getId())).findFirst();
            if(mealReservationOptional.isPresent()) {
                MealReservation old = mealReservationOptional.get();
                monthlyMealReservations.getMealReservations().remove(old);
                monthlyMealReservations.getMealReservations().add(mealReservation);
            } else {
                monthlyMealReservations.getMealReservations().add(mealReservation);
            }
            mealReservationRepository.save(monthlyMealReservations);
        } else {
            MonthlyMealReservations monthlyMealReservations = new MonthlyMealReservations(id, List.of(mealReservation));
            mealReservationRepository.save(monthlyMealReservations);
        }
    }

    public void removeReservation(MealReservation mealReservation) {
        String id = mealReservation.getReservationDate().getYear() + "-" + mealReservation.getReservationDate().getMonth()+"_reservation";
        Optional<MonthlyMealReservations> dailyMealReservationsOptional = mealReservationRepository.getMonthlyMealReservationsById(id);
        if (dailyMealReservationsOptional.isPresent()) {
            MonthlyMealReservations monthlyMealReservations = dailyMealReservationsOptional.get();
            int oldSize = monthlyMealReservations.getMealReservations().size();
            if (StringUtils.isNotBlank(mealReservation.getId()) ) {
                monthlyMealReservations.getMealReservations().removeIf(r -> r.getId().equals(mealReservation.getId()));
            } else {
               String resId = mealReservation.getEmployeeNumber() + "_" + mealReservation.getReservationDate();
                monthlyMealReservations.getMealReservations().removeIf(r -> r.getId().equals(resId));
            }
            if(monthlyMealReservations.getMealReservations().size() == oldSize) {
                throw new InvalidRequestException("Reservation does not exit for employeeNumber " + mealReservation.getEmployeeNumber() + " for date: " + mealReservation.getReservationDate());
            }
            mealReservationRepository.save(monthlyMealReservations);
        }
    }
}
