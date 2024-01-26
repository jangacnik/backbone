package com.resort.platform.backnode.taskmanager.controller;

import com.resort.platform.backnode.foodtracker.model.Department;
import com.resort.platform.backnode.foodtracker.model.rest.response.FoodTrackerUserWithDepartment;
import com.resort.platform.backnode.foodtracker.service.DepartmentService;
import com.resort.platform.backnode.foodtracker.service.FoodTrackerUserService;
import com.resort.platform.backnode.taskmanager.model.TaskListArchiveModel;
import com.resort.platform.backnode.taskmanager.model.rest.request.ArchiveTaskListRequest;
import com.resort.platform.backnode.taskmanager.model.rest.request.TaskStatusChangeRequest;
import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import com.resort.platform.backnode.taskmanager.service.TaskListArchiveService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task/archive")
public class TaskListArchiveController {

  @Autowired
  private TaskListArchiveService taskListArchiveService;
  @Autowired
  private FoodTrackerUserService foodTrackerUserService;

  @Autowired
  private DepartmentService departmentService;

  @GetMapping
  public ResponseEntity<List<TaskListArchiveModel>> getTaskListByDate(@RequestBody
  ArchiveTaskListRequest request) {
    return ResponseEntity.ok(
        taskListArchiveService.getAllUserTasksListByDate(request.getDepartment(),
            request.getDate()));
  }
  @GetMapping("/{date}")
  public ResponseEntity<List<TaskListArchiveModel>> getTaskListByUser(@RequestHeader(name = "Authorization") String token, @PathVariable String date) {

    FoodTrackerUserWithDepartment usr = foodTrackerUserService.getCurrentUser(token);
    List<Department> userDepartments = departmentService.getAllDepartments();
    userDepartments = userDepartments.stream().filter(department -> usr.getDepartments().contains(department.getDepartmentName())).collect(
        Collectors.toList());
//    List<ShortDepartmentModel> userShortDepartments = Collections.emptyList();

    List<TaskListArchiveModel> tasks = new java.util.ArrayList<>(Collections.emptyList());
    for (Department dep: userDepartments) {
      tasks.addAll(taskListArchiveService.getAllUserTasksListByDate(new ShortDepartmentModel(dep.getId(), dep.getDepartmentName()),
          LocalDate.parse(date)));
    }
    return ResponseEntity.ok(tasks);
  }

  @PostMapping("/status")
  public ResponseEntity<TaskListArchiveModel> updateTaskStatus(
      @RequestBody TaskStatusChangeRequest statusChangeRequest) {
    return ResponseEntity.ok(taskListArchiveService.changeTaskCompletedStatus(statusChangeRequest));
  }
}
