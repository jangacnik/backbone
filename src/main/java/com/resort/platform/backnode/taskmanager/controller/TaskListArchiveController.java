package com.resort.platform.backnode.taskmanager.controller;

import com.resort.platform.backnode.taskmanager.model.TaskListArchiveModel;
import com.resort.platform.backnode.taskmanager.model.rest.request.TaskStatusChangeRequest;
import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import com.resort.platform.backnode.taskmanager.service.TaskListArchiveService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task/archive")
public class TaskListArchiveController {

  @Autowired
  private TaskListArchiveService taskListArchiveService;

  @GetMapping
  public ResponseEntity<List<TaskListArchiveModel>> getTaskListByDate(@RequestBody
      ShortDepartmentModel department, @RequestBody LocalDate date) {
    return ResponseEntity.ok(taskListArchiveService.getAllUserTasksListByDate(department,date));
  }

  @PostMapping("/status")
  public ResponseEntity<TaskListArchiveModel> updateTaskStatus(@RequestBody TaskStatusChangeRequest statusChangeRequest) {
    return ResponseEntity.ok(taskListArchiveService.changeTaskCompletedStatus(statusChangeRequest));
  }
}
