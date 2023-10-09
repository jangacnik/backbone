package com.resort.platform.backnode.foodtracker.repo;

import com.resort.platform.backnode.foodtracker.model.MealPrice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MealPricerepository extends MongoRepository<MealPrice, String> {
    Optional<MealPrice> findFirstByOrderByLastUpdatedDesc();
}
