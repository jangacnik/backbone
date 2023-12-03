package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.auth.model.User;
import com.resort.platform.backnode.auth.repo.UserRepository;
import com.resort.platform.backnode.auth.service.AuthenticationService;
import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.rest.request.NewFoodTrackerUserRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FoodTrackerUserService {

  private AuthenticationService authenticationService;
  private UserRepository userRepository;
  private DepartmentService departmentService;

  public void addNewFoodTrackerUser(NewFoodTrackerUserRequest newFoodTrackerUserRequest) {
    if (StringUtils.isBlank(newFoodTrackerUserRequest.getEmployeeNumber())) {
      throw new InvalidRequestException("Employee number is required");
    }
    authenticationService.addNewUser(newFoodTrackerUserRequest);
    if (!CollectionUtils.isEmpty(newFoodTrackerUserRequest.getDepartments())) {
      for (String dep : newFoodTrackerUserRequest.getDepartments()) {
        departmentService.addEmployeeToDepartment(newFoodTrackerUserRequest.getEmployeeNumber(),
            dep);
      }
    }
  }

  public FoodTrackerUserWithDepartment getFoodTrackerUser(String username) {
    Optional<User> userOptional = userRepository.findUserByEmailOrEmployeeNumber(username,
        username);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      List<Department> departments = departmentService.getDepartmentsWithUser(
          user.getEmployeeNumber());
      List<String> departmentNames = new ArrayList<>();
      for (Department dep : departments) {
        departmentNames.add(dep.getDepartmentName());
      }
      return FoodTrackerUserWithDepartment.builder()
          .departments(departmentNames)
          .firstName(user.getFirstName())
          .lastName(user.getLastName())
          .email(user.getEmail())
          .employeeNumber(user.getEmployeeNumber())
          .build();
    }
    throw new UsernameNotFoundException("User not found");
  }

  public User deleteFoodTrackerUser(String username) {
    Optional<User> userOptional = userRepository.deleteUserByEmail(username);
    if (userOptional.isPresent()) {
      User deletedUser = userOptional.get();
      List<Department> optionalDepartmentList = departmentService.getDepartmentsWithUser(
          deletedUser.getEmployeeNumber());

      for (Department dep : optionalDepartmentList) {
        dep.getEmployees().remove(deletedUser.getEmployeeNumber());
        departmentService.saveOrUpdateDepartment(dep);
      }
      return deletedUser;
    }
    return null;
  }


}
