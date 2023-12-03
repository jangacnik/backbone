package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.exception.DepartmentNotFoundException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.rest.request.AddEmployeeToDepartmentRequest;
import com.resort.platform.backnode.foodtracker.model.rest.request.DepartmentDeletionRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.DepartmentWithUsersResponse;
import com.resort.platform.backnode.foodtracker.service.DepartmentService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

  Logger logger = LoggerFactory.getLogger(DepartmentController.class);
  @Autowired
  private DepartmentService departmentService;

  /**
   * Adds new deparment
   *
   * @param department - deparment object
   * @return - OK on success, exception if deparment already exists
   */
  @PostMapping
  public ResponseEntity<Void> addNewDepartment(@RequestBody Department department) {
    departmentService.addNewDepartment(department);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Adds employee with EmployeeNumber to department
   *
   * @param bodyData - department name and employeeNumber
   * @return - OK on success
   */
  @PostMapping("/employee")
  public ResponseEntity<Void> addEmployeeToDepartment(
      @RequestBody AddEmployeeToDepartmentRequest bodyData) {
    departmentService.addEmployeeToDepartmentByDepartmentName(bodyData.getEmployeeNumber(),
        bodyData.getDepartment());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Returns a deparment with complete data, including employees
   *
   * @param department - name of department
   * @return DepartmentWithUser
   * @throws DepartmentNotFoundException
   */
  @GetMapping("/{department}")
  public ResponseEntity<DepartmentWithUsersResponse> getDepratmentByName(
      @PathVariable String department) throws DepartmentNotFoundException {
    return ResponseEntity.ok(departmentService.getDepartmentByName(department));
  }

  /**
   * Returns list of all deparments
   *
   * @return list of departments
   */
  @GetMapping("/all")
  public ResponseEntity<List<Department>> getListOfAllDepartments() {
    return ResponseEntity.ok(departmentService.getAllDepartments());
  }

  /**
   * Returns list of all department names
   *
   * @return list of all department names
   */
  @GetMapping("/all/names")
  public ResponseEntity<List<String>> getListOfAllDepartmentsName() {
    return ResponseEntity.ok(departmentService.getAllDepartmentsNames());
  }

  /**
   * Removes employee from department
   *
   * @param departmentDeletionRequest - employeeNumber and department name
   * @return OK if success
   */
  @DeleteMapping
  public ResponseEntity<Void> deleteEmployeeFromDepartment(
      @RequestBody DepartmentDeletionRequest departmentDeletionRequest) {
    departmentService.removeUserFromDepartment(departmentDeletionRequest.getEmployeeNumber(),
        departmentDeletionRequest.getDepartmentName());
    return ResponseEntity.ok(null);
  }

}
