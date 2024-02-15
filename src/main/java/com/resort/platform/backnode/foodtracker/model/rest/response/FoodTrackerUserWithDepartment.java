package com.resort.platform.backnode.foodtracker.model.rest.response;

import com.resort.platform.backnode.auth.model.enums.Role;
import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FoodTrackerUserWithDepartment extends FoodTrackerUser {

  List<ShortDepartmentModel> departments;
  List<Role> roles;
  String oldEmail;

  @Builder
  public FoodTrackerUserWithDepartment(String id, String lastName, String firstName, String email,
      String employeeNumber, List<ShortDepartmentModel> departments, List<Role> roles) {
    super(id, lastName, firstName, email, employeeNumber);
    this.departments = departments;
    this.roles = roles;
  }
}
