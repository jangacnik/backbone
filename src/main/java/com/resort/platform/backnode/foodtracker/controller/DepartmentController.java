package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.exception.DepartmentNotFoundException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.rest.request.AddEmployeeToDepartmentRequest;
import com.resort.platform.backnode.foodtracker.model.rest.request.DepartmentDeletionRequest;
import com.resort.platform.backnode.foodtracker.model.rest.response.DepartmentWithUsersResponse;
import com.resort.platform.backnode.foodtracker.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    Logger logger = LoggerFactory.getLogger(DepartmentController.class);


    @PostMapping
    public ResponseEntity<Void> addNewDepartment(@RequestBody Department department) {
        departmentService.addNewDepartment(department);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/employee")
    public ResponseEntity<Void> addEmployeeToDepartment(@RequestBody AddEmployeeToDepartmentRequest bodyData) {
        departmentService.addEmployeeToDepartmentByDepartmentName(bodyData.getEmployeeNumber(), bodyData.getDepartment());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{department}")
    public ResponseEntity<DepartmentWithUsersResponse> getDepratmentByName(@PathVariable String department) throws DepartmentNotFoundException {
        return ResponseEntity.ok(departmentService.getDepartmentByName(department));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Department>> getListOfAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/all/names")
    public ResponseEntity<List<String>> getListOfAllDepartmentsName() {
        return ResponseEntity.ok(departmentService.getAllDepartmentsNames());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEmployeeFromDepartment(@RequestBody DepartmentDeletionRequest departmentDeletionRequest) {
        departmentService.removeUserFromDepartment(departmentDeletionRequest.getEmployeeNumber(), departmentDeletionRequest.getDepartmentName());
        return ResponseEntity.ok(null);
    }

}
