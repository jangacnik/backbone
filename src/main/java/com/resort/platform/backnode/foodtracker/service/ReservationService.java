package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.foodtracker.model.DailyMealReservations;
import com.resort.platform.backnode.foodtracker.model.MealReservation;
import com.resort.platform.backnode.foodtracker.repo.MealReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {
    private MealReservationRepository mealReservationRepository;

    public DailyMealReservations getReservation(LocalDate localDate) {
        Optional<DailyMealReservations> dailyMealReservationsOptional = mealReservationRepository.getDailyMealReservationsById(localDate.toString()+"_reservation");
        return dailyMealReservationsOptional.orElse(null);
    }

    public void addReservationForNextDay(MealReservation mealReservation) {
        LocalDate localDate = mealReservation.getReservationDate();
        Optional<DailyMealReservations> dailyMealReservationsOptional = mealReservationRepository.getDailyMealReservationsById(localDate.toString()+"_reservation");
        if (dailyMealReservationsOptional.isPresent()) {
            DailyMealReservations dailyMealReservations = dailyMealReservationsOptional.get();
            dailyMealReservations.getMealReservations().add(mealReservation);
            mealReservationRepository.save(dailyMealReservations);
        } else {
            DailyMealReservations dailyMealReservations = new DailyMealReservations(localDate+"_reservation", localDate, List.of(mealReservation));
            mealReservationRepository.save(dailyMealReservations);
        }
    }
}
