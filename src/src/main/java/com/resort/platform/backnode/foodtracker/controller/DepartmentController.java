package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.exception.DepartmentNotFoundException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.rest.request.AddEmployeeToDepartmentRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.DepartmentWithUsersResponse;
import com.resort.platform.backnode.foodtracker.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

  @Autowired
  private DepartmentService departmentService;

  @PostMapping
  public ResponseEntity<Void> addNewDepartment(@RequestBody Department department) {
    departmentService.addNewDepartment(department);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/employee")
  public ResponseEntity<Void> addEmployeeToDepartment(
      @RequestBody AddEmployeeToDepartmentRequest bodyData) {
    departmentService.addEmployeeToDepartment(bodyData.getEmployeeNumber(),
        bodyData.getDepartment());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{department}")
  public ResponseEntity<DepartmentWithUsersResponse> getDepratmentByName(
      @PathVariable String department) throws DepartmentNotFoundException {
    return ResponseEntity.ok(departmentService.getDepartmentByName(department));
  }

  @GetMapping("/all")
  public ResponseEntity<List<Department>> getListOfAllDepartments() {
    return ResponseEntity.ok(new ArrayList<>());
  }

}
