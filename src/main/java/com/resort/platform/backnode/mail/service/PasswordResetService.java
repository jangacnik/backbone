package com.resort.platform.backnode.mail.service;

import com.resort.platform.backnode.foodtracker.service.FoodTrackerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

  @Autowired
  private FoodTrackerUserService foodTrackerUserService;


  public String resetPassword(String email) {
    return foodTrackerUserService.resetFoodTrackerUserPassword(email);
  }
}
