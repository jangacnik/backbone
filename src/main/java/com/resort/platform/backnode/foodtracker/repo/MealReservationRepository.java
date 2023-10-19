package com.resort.platform.backnode.foodtracker.repo;

import com.resort.platform.backnode.foodtracker.model.DailyMealReservations;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MealReservationRepository extends MongoRepository<DailyMealReservations, String> {
    Optional<DailyMealReservations> getDailyMealReservationsById(String id);
}
