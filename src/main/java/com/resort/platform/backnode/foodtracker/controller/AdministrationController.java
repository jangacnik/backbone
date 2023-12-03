package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.auth.model.enums.Role;
import com.resort.platform.backnode.foodtracker.exception.DepartmentAlreadyExistsException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.planday.DepartmentResponseModel;
import com.resort.platform.backnode.foodtracker.model.planday.DepartmentSubModel;
import com.resort.platform.backnode.foodtracker.model.planday.EmployeeSubModel;
import com.resort.platform.backnode.foodtracker.model.planday.UserResponseModel;
import com.resort.platform.backnode.foodtracker.model.rest.request.NewFoodTrackerUserRequest;
import com.resort.platform.backnode.foodtracker.service.AdministrationService;
import com.resort.platform.backnode.foodtracker.service.DepartmentService;
import com.resort.platform.backnode.foodtracker.service.FoodTrackerUserService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/admin")
public class AdministrationController {

  /**
   * Data for accessing playday api, all encrypted in config with jasypt
   */
  @Value("${com.resort.platform.backnode.planday.clientid}")
  private String clientId;
  @Value("${com.resort.platform.backnode.planday.token}")
  private String token;
  @Value("${com.resort.platform.backnode.planday.url.token.get}")
  private String tokenUrl;
  @Value("${com.resort.platform.backnode.planday.url.departments.get}")
  private String departmentUrl;
  @Value("${com.resort.platform.backnode.planday.url.employees.get}")
  private String employeesUrl;

  @Autowired
  private AdministrationService administrationService;
  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private FoodTrackerUserService foodTrackerUserService;

  /**
   * 1. Calls Planday API to get access token 2. Calls Departments API to get all departments and
   * updates the list of departments if new were added 3. Calls Employee API to fetch all employees,
   * does that in batches of 50, adds them also to their departments
   *
   * @return OK if successful
   * @throws IOException
   * @throws URISyntaxException
   */
  @PostMapping("/update")
  public ResponseEntity<Void> updateEmployeesAndDepartmentsFromPlandayRest()
      throws IOException, URISyntaxException {
    String accessToken = administrationService.getToken(tokenUrl, token, clientId);
    DepartmentResponseModel res = administrationService.getDepartments(departmentUrl, accessToken,
        clientId);
    for (DepartmentSubModel dep : res.getData()) {
      try {
        departmentService.addNewDepartment(
            new Department(String.valueOf(dep.getId()), dep.getName(), new ArrayList<>()));
      } catch (DepartmentAlreadyExistsException e) {
        // do not terminate on exception
      }
    }
    int total;
    int offset = 0;
    UserResponseModel userResponseModel = administrationService.getEmployees(employeesUrl,
        accessToken, clientId, offset);
    total = userResponseModel.getPaging().getTotal();
    List<Role> roles = new ArrayList<>();
    roles.add(Role.USER);
    while (userResponseModel.getPaging().getOffset() < total) {
      userResponseModel = administrationService.getEmployees(employeesUrl, accessToken, clientId,
          offset);
      for (EmployeeSubModel emp : userResponseModel.getData()) {
        try {
          NewFoodTrackerUserRequest usr = new NewFoodTrackerUserRequest();
          usr.setEmail(emp.getEmail());
          usr.setFirstName(emp.getFirstName());
          usr.setLastName(emp.getLastName());
          usr.setDepartments(emp.getDepartments());
          usr.setEmployeeNumber(String.valueOf(emp.getId()));
          usr.setPassword(usr.getLastName() + usr.getEmployeeNumber());
          usr.setRoles(roles);
          foodTrackerUserService.addNewFoodTrackerUser(usr, true);
        } catch (Exception e) {

        }
      }
      offset += 50;
    }
    return ResponseEntity.ok(null);
  }

}
