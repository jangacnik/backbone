package com.resort.platform.backnode.foodtracker.repo;

import com.resort.platform.backnode.foodtracker.model.MonthlyMealReservations;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MealReservationRepository extends MongoRepository<MonthlyMealReservations, String> {
    Optional<MonthlyMealReservations> getMonthlyMealReservationsById(String id);

    Optional<List<MonthlyMealReservations>> getAllByIdContaining(String idSubstring);
}
