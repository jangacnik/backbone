package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.auth.model.User;
import com.resort.platform.backnode.auth.repo.UserRepository;
import com.resort.platform.backnode.auth.service.AuthenticationService;
import com.resort.platform.backnode.auth.service.JwtService;
import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.rest.request.NewFoodTrackerUserRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@AllArgsConstructor
public class FoodTrackerUserService {

  private AuthenticationService authenticationService;
  private UserRepository userRepository;
  private DepartmentService departmentService;
  private JwtService jwtService;

  /**
   * Adds the given user to the database if he does not already exist. Adds them to the department
   * by name or ID depending on the auto flag
   *
   * @param newFoodTrackerUserRequest - object with user data and department list
   * @param auto                      - true if the user is added from Planday import
   */
  public void addNewFoodTrackerUser(NewFoodTrackerUserRequest newFoodTrackerUserRequest,
      boolean auto) {
    if (StringUtils.isBlank(newFoodTrackerUserRequest.getEmployeeNumber())) {
      throw new InvalidRequestException("Employee number is required");
    }
    if (StringUtils.isBlank(newFoodTrackerUserRequest.getPassword())) {
      newFoodTrackerUserRequest.setPassword(
          newFoodTrackerUserRequest.getFirstName() + newFoodTrackerUserRequest.getEmployeeNumber());
    }
    authenticationService.addNewUser(newFoodTrackerUserRequest);
    if (!CollectionUtils.isEmpty(newFoodTrackerUserRequest.getDepartments())) {
      for (String dep : newFoodTrackerUserRequest.getDepartments()) {
        if (auto) {
          departmentService.addEmployeeToDepartmentByDepartmentId(
              newFoodTrackerUserRequest.getEmployeeNumber(), dep);
        } else {
          departmentService.addEmployeeToDepartmentByDepartmentName(
              newFoodTrackerUserRequest.getEmployeeNumber(), dep);
        }
      }
    }
  }


  /**
   * Get food tracker user by his username (E-mail)
   *
   * @param username - E-mail of the user
   * @return User with list of his departments
   */
  public FoodTrackerUserWithDepartment getFoodTrackerUser(String username) {
    Optional<User> userOptional = userRepository.findUserByEmployeeNumberOrEmail(username,
        username);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      List<Department> departments = departmentService.getDepartmentsWithUser(
          user.getEmployeeNumber());
      List<ShortDepartmentModel> departmentNames = new ArrayList<>();
      for (Department dep : departments) {
        departmentNames.add(new ShortDepartmentModel(dep.getId(),dep.getDepartmentName()));
      }
      return FoodTrackerUserWithDepartment.builder().departments(departmentNames)
          .firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail())
          .employeeNumber(user.getEmployeeNumber()).roles(user.getRoles()).id(user.getId()).build();
    }
    throw new UsernameNotFoundException("User not found");
  }

  /**
   * Returns all users from the database
   *
   * @return list of all users including departments of each user
   */
  public List<FoodTrackerUserWithDepartment> getAllFoodTrackerUsers() {
    List<User> userList = userRepository.findAll();
    if (!CollectionUtils.isEmpty(userList)) {
      List<FoodTrackerUserWithDepartment> usersWithDepartments = new ArrayList<>();
      List<Department> departments = departmentService.getAllDepartments();
      for (User us : userList) {
        List<Department> departmentNames = departments.stream()
            .filter((department -> department.getEmployees().contains(us.getEmployeeNumber()))).toList();
        List<ShortDepartmentModel> shModel = new ArrayList<>();
        for (Department dp : departmentNames) {
          shModel.add(new ShortDepartmentModel(dp.getId(), dp.getDepartmentName()));
        }
        usersWithDepartments.add(FoodTrackerUserWithDepartment.builder()
            .id(us.getId())
            .departments(shModel).firstName(us.getFirstName()).lastName(us.getLastName())
            .email(us.getEmail()).employeeNumber(us.getEmployeeNumber()).roles(us.getRoles())
            .build());
      }
      return usersWithDepartments;
    }
    throw new UsernameNotFoundException("Users not found");
  }

  /**
   * Deletes userdata by username (E-mail)
   *
   * @param username - Email of the user
   * @return deleted user
   */
  public User deleteFoodTrackerUser(String username) {
    Optional<User> userOptional = userRepository.deleteUserByEmployeeNumber(username);
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

  /**
   * Get user from JWT token
   *
   * @param token - JWT token
   * @return user date of the user
   */
  public FoodTrackerUserWithDepartment getCurrentUser(String token) {
    String jwt = token.substring(7);
    String un = jwtService.extractUserName(jwt);
    return this.getFoodTrackerUser(un);
  }

  /**
   * Updates user data of the user
   *
   * @param updatedUser - updated user data with all needed information for update
   */
  public void updateUser(FoodTrackerUserWithDepartment updatedUser) {
    String id = updatedUser.getId();
    User old = userRepository.findById(id).get();
    userRepository.save(
        new User(old.getId(), updatedUser.getEmployeeNumber(), updatedUser.getFirstName(),
            updatedUser.getLastName(), updatedUser.getEmail(), old.getPassword(),
            updatedUser.getRoles()));
    List<Department> departments = departmentService.getDepartmentsWithUser(
        updatedUser.getEmployeeNumber());
    for (Department dep : departments) {
      if (dep.getEmployees().contains(updatedUser.getEmployeeNumber())
          && !updatedUser.getDepartments().contains(dep.getDepartmentName())) {
        departmentService.removeUserFromDepartment(updatedUser.getEmployeeNumber(),
            dep.getDepartmentName());
      }
    }
    for (ShortDepartmentModel dep : updatedUser.getDepartments()) {
      departmentService.addEmployeeToDepartmentByDepartmentName(updatedUser.getEmployeeNumber(),
          dep.getDepartmentName());
    }
  }
}
