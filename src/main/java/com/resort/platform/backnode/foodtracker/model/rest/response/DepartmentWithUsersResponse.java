package com.resort.platform.backnode.foodtracker.model.rest.response;

import com.resort.platform.backnode.foodtracker.model.Department;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DepartmentWithUsersResponse extends Department {

  private final List<FoodTrackerUser> users = new ArrayList<>();

  @Builder
  public DepartmentWithUsersResponse(String id, String departmentName, ArrayList<String> employees,
      List<FoodTrackerUser> users) {
    super(id, departmentName, employees);
    this.users.addAll(users);
  }
}
