package com.resort.platform.backnode.foodtracker.repo;

import com.resort.platform.backnode.foodtracker.model.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends MongoRepository<Department, String> {

  Optional<Department> getDepartmentByDepartmentName(String departmentName);

  Optional<List<Department>> findAllByEmployeesContaining(String employeeId);

}
