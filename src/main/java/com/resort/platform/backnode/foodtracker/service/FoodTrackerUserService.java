package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.auth.service.AuthenticationService;
import com.resort.platform.backnode.foodtracker.exception.InvalidRequestException;
import com.resort.platform.backnode.foodtracker.model.rest.request.NewFoodTrackerUserRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@AllArgsConstructor
public class FoodTrackerUserService {
    private AuthenticationService authenticationService;
    private DepartmentService departmentService;

    public void addNewFoodTrackerUser(NewFoodTrackerUserRequest newFoodTrackerUserRequest) {
        if (StringUtils.isBlank(newFoodTrackerUserRequest.getEmployeeNumber())) {
            throw new InvalidRequestException("Employee number is required");
        }
        authenticationService.addNewUser(newFoodTrackerUserRequest);
        if (!CollectionUtils.isEmpty(newFoodTrackerUserRequest.getDepartments())) {
            for (String dep: newFoodTrackerUserRequest.getDepartments()) {
                departmentService.addEmployeeToDepartment(newFoodTrackerUserRequest.getEmployeeNumber(),dep);
            }
        }
    }

}
