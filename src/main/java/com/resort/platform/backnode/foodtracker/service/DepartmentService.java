package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.foodtracker.exception.DepartmentAlreadyExistsException;
import com.resort.platform.backnode.foodtracker.exception.DepartmentNotFoundException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.repo.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public void addNewDepartment(Department department) throws DepartmentAlreadyExistsException {
        Optional<Department> departmentOptional = departmentRepository.getDepartmentByDepartmentName(department.getDepartmentName());
        if(departmentOptional.isPresent()) {
            throw new DepartmentAlreadyExistsException("Department " + department.getDepartmentName() + " already exists");
        }
        departmentRepository.save(department);
    }

    public Department getDepartmentByName(String departmentName) throws DepartmentNotFoundException {
        return departmentRepository.getDepartmentByDepartmentName(departmentName)
                .orElseThrow(
                () -> new DepartmentNotFoundException("Department: " + departmentName + " not found")
        );
    }

    public void addEmployeeToDepartment(String employeeNumber, String departmentName) {
        Optional<Department> departmentOptional = departmentRepository.getDepartmentByDepartmentName(departmentName);
        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            if(CollectionUtils.isEmpty(department.getEmployees())) {
                department.setEmployees(new ArrayList<>(List.of(employeeNumber)));
            } else {
                if(!department.getEmployees().contains(employeeNumber)) {
                    department.getEmployees().add(employeeNumber);
                }
            }
            departmentRepository.save(department);
        } else {
            throw new DepartmentNotFoundException("Department: " + departmentName + " not found");
        }
    }
}
