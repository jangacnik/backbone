package com.resort.platform.backnode.foodtracker.controller;

import com.resort.platform.backnode.foodtracker.exception.DepartmentNotFoundException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{department}")
    public ResponseEntity<Department> getDepratmentByName(String department) throws DepartmentNotFoundException {
        return ResponseEntity.ok(departmentService.getDepartmentByName(department));
    }
}
