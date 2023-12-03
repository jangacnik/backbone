package com.resort.platform.backnode.foodtracker.repo;

import com.resort.platform.backnode.foodtracker.model.MealTracking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackingRepository extends MongoRepository<MealTracking, String> {

}
