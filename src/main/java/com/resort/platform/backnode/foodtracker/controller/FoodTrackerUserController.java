package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.auth.model.User;
import com.resort.platform.backnode.foodtracker.model.rest.request.FoodTrackerUserRequest;
import com.resort.platform.backnode.foodtracker.model.rest.request.NewFoodTrackerUserRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import com.resort.platform.backnode.foodtracker.service.FoodTrackerUserService;
import com.resort.platform.backnode.taskmanager.model.util.ShortUserModel;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/department/user")
public class FoodTrackerUserController {
@Autowired
  private FoodTrackerUserService foodTrackerUserService;

  /**
   * Creates new user and also adds him to departments
   *
   * @param newFoodTrackerUserRequest - employee with departments
   * @return OK if success
   */
  @PostMapping
  public ResponseEntity<Void> addNewFoodTrackerUser(
      @RequestBody NewFoodTrackerUserRequest newFoodTrackerUserRequest) {
    foodTrackerUserService.addNewFoodTrackerUser(newFoodTrackerUserRequest, false);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Removes employee by ID and also removes him from all departments
   *
   * @param id - ID of employee
   * @return deleted user
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<User> deleteFoodTrackerUser(@PathVariable String id) {
    return ResponseEntity.ok(foodTrackerUserService.deleteFoodTrackerUser(id));

  }

  /**
   * Updates Employee data
   *
   * @param foodTrackerUserWithDepartment - updated data of user with deparments
   * @return updated user
   */
  @PutMapping
  public ResponseEntity<FoodTrackerUserWithDepartment> updateFoodTrackerUser(
      @RequestBody FoodTrackerUserWithDepartment foodTrackerUserWithDepartment) {
    foodTrackerUserService.updateUser(foodTrackerUserWithDepartment);
    return ResponseEntity.ok(null);
  }

  /**
   * Returns full data of user with username
   *
   * @param fUser - object with username of user
   * @return FoodTrackerUserWithDepartment of user with given username
   */
  @GetMapping
  public ResponseEntity<FoodTrackerUserWithDepartment> getFoodTrackerUser(
      @RequestBody FoodTrackerUserRequest fUser) {
    return ResponseEntity.ok(foodTrackerUserService.getFoodTrackerUser(fUser.getUsername()));
  }

  /**
   * Returns all employees
   *
   * @return list of all employees
   */
  @GetMapping("/all")
  public ResponseEntity<List<FoodTrackerUserWithDepartment>> getFoodTrackerUser() {
    return ResponseEntity.ok(foodTrackerUserService.getAllFoodTrackerUsers());
  }


  /**
   * Returns Employee data from user via his JWT token
   *
   * @param token - jwt token of user
   * @return employee data of user
   */
  @GetMapping("/me")
  public ResponseEntity<FoodTrackerUserWithDepartment> getCurretnFoodTrackerUser(
      @RequestHeader(name = "Authorization") String token) {
    return ResponseEntity.ok(foodTrackerUserService.getCurrentUser(token));
  }
}
