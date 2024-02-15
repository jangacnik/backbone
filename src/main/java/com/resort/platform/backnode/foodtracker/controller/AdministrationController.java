package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.auth.model.enums.Role;
import com.resort.platform.backnode.foodtracker.exception.DepartmentAlreadyExistsException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.planday.DepartmentResponseModel;
import com.resort.platform.backnode.foodtracker.model.planday.DepartmentSubModel;
import com.resort.platform.backnode.foodtracker.model.planday.EmployeeSubModel;
import com.resort.platform.backnode.foodtracker.model.planday.ShiftsModel;
import com.resort.platform.backnode.foodtracker.model.planday.ShiftsSubModel;
import com.resort.platform.backnode.foodtracker.model.planday.UserResponseModel;
import com.resort.platform.backnode.foodtracker.model.rest.request.NewFoodTrackerUserRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUser;
import com.resort.platform.backnode.foodtracker.service.AdministrationService;
import com.resort.platform.backnode.foodtracker.service.DepartmentService;
import com.resort.platform.backnode.foodtracker.service.FoodTrackerUserService;
import com.resort.platform.backnode.taskmanager.model.util.ShiftUserModel;
import com.resort.platform.backnode.taskmanager.model.util.ShortUserModel;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


  @Value("${com.resort.platform.backnode.planday.url.employees.shifts.get}")
  private String shiftsUrl;
  @Autowired
  private AdministrationService administrationService;
  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private FoodTrackerUserService foodTrackerUserService;


  private String accessToken  = null;

  @GetMapping("/token")
  public void getToken() throws IOException, URISyntaxException {
    accessToken = administrationService.getToken(tokenUrl, token, clientId);

  }

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
    accessToken = administrationService.getToken(tokenUrl, token, clientId);
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


  @GetMapping("/{date}/available/{dep}")
  public ResponseEntity<Object> getAvailableUsers(@PathVariable String date, @PathVariable String dep)
      throws IOException, URISyntaxException {
    accessToken = administrationService.getToken(tokenUrl, token, clientId);
    ShiftsModel shifts = administrationService.getAvailableEmployees(shiftsUrl,accessToken, clientId, dep, date);
    List<ShiftsSubModel> filteredShifts = shifts.getData().stream().filter(sh -> "Assigned".equals(sh.getStatus()) || "PunchclockStarted".equals(sh.getStatus())).toList();
    List<ShiftUserModel> shiftUserModelList = new ArrayList<>();
    for (ShiftsSubModel sub: filteredShifts) {
    try {
        FoodTrackerUser fd = foodTrackerUserService.getFoodTrackerUser(sub.getEmployeeId());
        if (fd != null) {
          ShiftUserModel su = new ShiftUserModel();
          su.setUser(new ShortUserModel(fd.getEmployeeNumber(),fd.getFirstName(), fd.getLastName()));
          su.setStartTime(sub.getStartDateTime().split("T")[0]);
          su.setEndTime(sub.getEndDateTime().split("T")[0]);
          shiftUserModelList.add(su);
        }
    } catch (Exception e) {
        String et = e.toString();
    }
    }

    return ResponseEntity.ok(shiftUserModelList);
  }
}
