package com.resort.platform.backnode.foodtracker.repo;

import com.resort.platform.backnode.foodtracker.model.MealPrice;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MealPricerepository extends MongoRepository<MealPrice, String> {

  Optional<MealPrice> findFirstByOrderByLastUpdatedDesc();
}
