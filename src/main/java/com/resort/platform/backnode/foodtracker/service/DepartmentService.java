package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.auth.model.User;
import com.resort.platform.backnode.auth.repo.UserRepository;
import com.resort.platform.backnode.foodtracker.exception.DepartmentAlreadyExistsException;
import com.resort.platform.backnode.foodtracker.exception.DepartmentNotFoundException;
import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.rest.response.DepartmentWithUsersResponse;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUser;
import com.resort.platform.backnode.foodtracker.repo.DepartmentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
public class DepartmentService {

  @Autowired
  private DepartmentRepository departmentRepository;
  @Autowired
  private UserRepository userRepository;

  /**
   * Adds new department to the databese
   *
   * @param department - new department to be added
   * @throws DepartmentAlreadyExistsException if deparment already exist
   */
  public void addNewDepartment(Department department) throws DepartmentAlreadyExistsException {
    Optional<Department> departmentOptional = departmentRepository.getDepartmentByDepartmentName(
        department.getDepartmentName());
    if (departmentOptional.isPresent()) {
      throw new DepartmentAlreadyExistsException(
          "Department " + department.getDepartmentName() + " already exists");
    }
    if (ObjectUtils.isEmpty(department.getEmployees())) {
      department.setEmployees(new ArrayList<>());
    }
    departmentRepository.save(department);
  }

  /**
   * Returns deparment with all included users by department name
   *
   * @param departmentName - deparment name
   * @return Deparment with list of users in the given deparment
   * @throws DepartmentNotFoundException if deparment with given name does not exist
   */
  public DepartmentWithUsersResponse getDepartmentByName(String departmentName)
      throws DepartmentNotFoundException {
    Department department = departmentRepository.getDepartmentByDepartmentName(departmentName)
        .orElseThrow(
            () -> new DepartmentNotFoundException("Department: " + departmentName + " not found")
        );
    List<FoodTrackerUser> foodTrackerUsers = new ArrayList<>();
    if (!CollectionUtils.isEmpty(department.getEmployees())) {
      for (String employeeId : department.getEmployees()) {
        Optional<User> userOptional = userRepository.findUserByEmployeeNumber(employeeId);
        if (userOptional.isPresent()) {
          User tempUser = userOptional.get();
          foodTrackerUsers.add(
              new FoodTrackerUser(tempUser.getId(), tempUser.getLastName(), tempUser.getFirstName(),
                  tempUser.getEmail(), tempUser.getEmployeeNumber()));
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

  /**
   * Adds employee with @param employeeNumber to department with @param departmentName
   *
   * @param employeeNumber - employeeNumber of the employee
   * @param departmentName - departmentName of the department
   */
  public void addEmployeeToDepartmentByDepartmentName(String employeeNumber,
      String departmentName) {
    Optional<Department> departmentOptional = departmentRepository.getDepartmentByDepartmentName(
        departmentName);
    if (departmentOptional.isEmpty()) {
      ArrayList<String> emp = new ArrayList<String>();
      emp.add(employeeNumber);
      addNewDepartment(new Department(departmentName, emp));
    } else {
      Department department = departmentOptional.get();
      if (CollectionUtils.isEmpty(department.getEmployees())) {
        department.setEmployees(new ArrayList<>(List.of(employeeNumber)));
      } else {
        if (!department.getEmployees().contains(employeeNumber)) {
          department.getEmployees().add(employeeNumber);
        }
      }
      departmentRepository.save(department);
    }
  }

  /**
   * Adds employee with @param employeeNumber to department with @param id
   *
   * @param employeeNumber - employeeNumber of employee
   * @param id             - id of department
   */
  public void addEmployeeToDepartmentByDepartmentId(String employeeNumber, String id) {
    Optional<Department> departmentOptional = departmentRepository.getDepartmentById(id);
    if (departmentOptional.isEmpty()) {
      throw new DepartmentNotFoundException("Department with id: " + id + "not found");
    } else {
      Department department = departmentOptional.get();
      if (CollectionUtils.isEmpty(department.getEmployees())) {
        department.setEmployees(new ArrayList<>(List.of(employeeNumber)));
      } else {
        if (!department.getEmployees().contains(employeeNumber)) {
          department.getEmployees().add(employeeNumber);
        }
      }
      departmentRepository.save(department);
    }
  }

  /**
   * @return list of all departments
   */
  public List<Department> getAllDepartments() {
    return departmentRepository.findAll();
  }

  /**
   * @return list of all department names
   */
  public List<String> getAllDepartmentsNames() {
    List<Department> deps = departmentRepository.findAll();
    return deps.stream().map(Department::getDepartmentName).collect(Collectors.toList());
  }

  /**
   * Get all departments with the given employee
   *
   * @param employeeNumber - employeeNumber of employee
   * @return list of all departments that include the given employeeNumber
   */
  public List<Department> getDepartmentsWithUser(String employeeNumber) {
    return departmentRepository.findAllByEmployeesContaining(employeeNumber)
        .orElseThrow(() -> new DepartmentNotFoundException("Departments with user not found"));
  }

  /**
   * Save changes to department
   *
   * @param department - department data
   * @return updated department
   */
  public Department saveOrUpdateDepartment(Department department) {
    return departmentRepository.save(department);
  }

  /**
   * Removes employee by EmployeeNumber from the department by department name
   *
   * @param employeeNumber - employee Number
   * @param departmentName - department name
   */
  public void removeUserFromDepartment(String employeeNumber, String departmentName) {
    Optional<Department> department = departmentRepository.getDepartmentByDepartmentName(
        departmentName);
    if (department.isPresent()) {
      Department department1 = department.get();
      department1.getEmployees().remove(employeeNumber);
      departmentRepository.save(department1);
    }
  }
}
