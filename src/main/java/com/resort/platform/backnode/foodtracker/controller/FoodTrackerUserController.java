package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.auth.model.User;
import com.resort.platform.backnode.foodtracker.model.rest.request.FoodTrackerUserRequest;
import com.resort.platform.backnode.foodtracker.model.rest.request.NewFoodTrackerUserRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import com.resort.platform.backnode.foodtracker.service.FoodTrackerUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department/user")
@AllArgsConstructor
public class FoodTrackerUserController {
    private FoodTrackerUserService foodTrackerUserService;

    @PostMapping
    public ResponseEntity<Void> addNewFoodTrackerUser(@RequestBody NewFoodTrackerUserRequest newFoodTrackerUserRequest) {
        foodTrackerUserService.addNewFoodTrackerUser(newFoodTrackerUserRequest, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteFoodTrackerUser(@PathVariable String id) {
        return ResponseEntity.ok(foodTrackerUserService.deleteFoodTrackerUser(id));

    }
    @PutMapping
    public ResponseEntity<FoodTrackerUserWithDepartment> updateFoodTrackerUser(@RequestBody FoodTrackerUserWithDepartment foodTrackerUserWithDepartment){
        foodTrackerUserService.updateUser(foodTrackerUserWithDepartment);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<FoodTrackerUserWithDepartment> getFoodTrackerUser(@RequestBody FoodTrackerUserRequest fUser) {
        return ResponseEntity.ok(foodTrackerUserService.getFoodTrackerUser(fUser.getUsername()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<FoodTrackerUserWithDepartment>> getFoodTrackerUser() {
        return ResponseEntity.ok(foodTrackerUserService.getAllFoodTrackerUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<FoodTrackerUserWithDepartment> getCurretnFoodTrackerUser(@RequestHeader (name="Authorization") String token) {
        return ResponseEntity.ok(foodTrackerUserService.getCurrentUser(token));
    }
}
