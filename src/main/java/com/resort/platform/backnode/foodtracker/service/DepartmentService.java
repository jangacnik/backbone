package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.auth.model.User;
import com.resort.platform.backnode.auth.repo.UserRepository;
import com.resort.platform.backnode.foodtracker.exception.DepartmentAlreadyExistsException;
import com.resort.platform.backnode.foodtracker.exception.DepartmentNotFoundException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.rest.response.DepartmentWithUsersResponse;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUser;
import com.resort.platform.backnode.foodtracker.repo.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    public void addNewDepartment(Department department) throws DepartmentAlreadyExistsException {
        Optional<Department> departmentOptional = departmentRepository.getDepartmentByDepartmentName(department.getDepartmentName());
        if(departmentOptional.isPresent()) {
            throw new DepartmentAlreadyExistsException("Department " + department.getDepartmentName() + " already exists");
        }
        if(ObjectUtils.isEmpty(department.getEmployees())) {
            department.setEmployees(new ArrayList<>());
        }
        departmentRepository.save(department);
    }

    public DepartmentWithUsersResponse getDepartmentByName(String departmentName) throws DepartmentNotFoundException {
        Department department = departmentRepository.getDepartmentByDepartmentName(departmentName).orElseThrow(
                () -> new DepartmentNotFoundException("Department: " + departmentName + " not found")
        );
        List<FoodTrackerUser> foodTrackerUsers = new ArrayList<>();
        if (!CollectionUtils.isEmpty(department.getEmployees())) {
            for (String employeeId : department.getEmployees()) {
                Optional<User> userOptional = userRepository.findUserByEmployeeNumber(employeeId);
                if (userOptional.isPresent()) {
                    User tempUser = userOptional.get();
                    foodTrackerUsers.add(new FoodTrackerUser(tempUser.getLastName(), tempUser.getFirstName(), tempUser.getEmail(), tempUser.getEmployeeNumber()));
                }
            }

        }
        return DepartmentWithUsersResponse
                .builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName())
                .employees(department.getEmployees())
                .users(foodTrackerUsers)
                .build();
    }

    public void addEmployeeToDepartmentByDepartmentName(String employeeNumber, String departmentName) {
        Optional<Department> departmentOptional = departmentRepository.getDepartmentByDepartmentName(departmentName);
        if (departmentOptional.isEmpty()) {
            ArrayList<String> emp = new ArrayList<String>();
            emp.add(employeeNumber);
            addNewDepartment(new Department(departmentName, emp));
        } else {
            Department department = departmentOptional.get();
            if(CollectionUtils.isEmpty(department.getEmployees())) {
                department.setEmployees(new ArrayList<>(List.of(employeeNumber)));
            } else {
                if(!department.getEmployees().contains(employeeNumber)) {
                    department.getEmployees().add(employeeNumber);
                }
            }
            departmentRepository.save(department);
        }
    }
    public void addEmployeeToDepartmentByDepartmentId(String employeeNumber, String id) {
        Optional<Department> departmentOptional = departmentRepository.getDepartmentById(id);
        if (departmentOptional.isEmpty()) {
            throw new DepartmentNotFoundException("Department with id: "+id + "not found");
        } else {
            Department department = departmentOptional.get();
            if(CollectionUtils.isEmpty(department.getEmployees())) {
                department.setEmployees(new ArrayList<>(List.of(employeeNumber)));
            } else {
                if(!department.getEmployees().contains(employeeNumber)) {
                    department.getEmployees().add(employeeNumber);
                }
            }
            departmentRepository.save(department);
        }
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    public List<String> getAllDepartmentsNames() {
        List<Department> deps =  departmentRepository.findAll();
        return deps.stream().map(Department::getDepartmentName).collect(Collectors.toList());
    }

    public List<Department> getDepartmentsWithUser(String employeeId) {
        return departmentRepository.findAllByEmployeesContaining(employeeId).orElseThrow(() -> new DepartmentNotFoundException("Departments with user not found"));
    }

    public Department saveOrUpdateDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public void removeUserFromDepartment(String employeeId, String departmentName) {
        Optional<Department> department = departmentRepository.getDepartmentByDepartmentName(departmentName);
        if (department.isPresent()) {
            Department department1 = department.get();
            department1.getEmployees().remove(employeeId);
            departmentRepository.save(department1);
        }
    }
}
