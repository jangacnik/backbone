package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.model.rest.request.NewFoodTrackerUserRequest;
import com.resort.platform.backnode.foodtracker.service.FoodTrackerUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/department/user")
@AllArgsConstructor
public class FoodTrackerUserController {
    private FoodTrackerUserService foodTrackerUserService;

    @PostMapping
    public ResponseEntity<Void> addNewFoodTrackerUser(@RequestBody NewFoodTrackerUserRequest newFoodTrackerUserRequest) {
        foodTrackerUserService.addNewFoodTrackerUser(newFoodTrackerUserRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
