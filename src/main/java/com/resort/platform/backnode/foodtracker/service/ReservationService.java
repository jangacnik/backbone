package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.foodtracker.model.MonthlyMealReservations;
import com.resort.platform.backnode.foodtracker.model.MealReservation;
import com.resort.platform.backnode.foodtracker.repo.MealReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {
    private MealReservationRepository mealReservationRepository;

    public List<MealReservation> getReservationsByDate(LocalDate localDate) {

        String id = localDate.getYear() + "-" + localDate.getMonth()+"_reservation";
        Optional<MonthlyMealReservations> monthlyMealReservationsOptional = mealReservationRepository.getMonthlyMealReservationsById(id);

        if(monthlyMealReservationsOptional.isPresent()) {
            MonthlyMealReservations monthlyMealReservations = monthlyMealReservationsOptional.get();
            List<MealReservation> mealReservations = monthlyMealReservations.getMealReservations().stream().filter(res -> res.getReservationDate().getMonth().equals(localDate.getMonth()) && localDate.getDayOfMonth() == res.getReservationDate().getDayOfMonth()).collect(Collectors.toList());
            return  mealReservations;
        }
        return null;
    }

    public void addReservationForNextDay(MealReservation mealReservation) {
        mealReservation.setId(mealReservation.getEmployeeNumber()+ "_" + mealReservation.getReservationDate().toString());
        String id = mealReservation.getReservationDate().getYear() + "-" + mealReservation.getReservationDate().getMonth()+"_reservation";
        Optional<MonthlyMealReservations> dailyMealReservationsOptional = mealReservationRepository.getMonthlyMealReservationsById(id);
        if (dailyMealReservationsOptional.isPresent()) {
            MonthlyMealReservations monthlyMealReservations = dailyMealReservationsOptional.get();
            monthlyMealReservations.getMealReservations().add(mealReservation);
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
            monthlyMealReservations.getMealReservations().remove(mealReservation);
            mealReservationRepository.save(monthlyMealReservations);
        }
    }
}
