package com.resort.platform.backnode.taskmanager.controller;

import com.resort.platform.backnode.taskmanager.model.TaskListTemplateModel;
import com.resort.platform.backnode.taskmanager.service.TaskListTemplateService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task/template")
public class TaskListTemplateController {

  @Autowired
  private TaskListTemplateService taskListTemplateService;

  @PostMapping
  public ResponseEntity<Void> createNewTaskListTemplate(@RequestBody @Valid TaskListTemplateModel templateModel) {
    this.taskListTemplateService.createTaskListTemplate(templateModel);
    return ResponseEntity.ok(null);
  }

  @GetMapping
  public ResponseEntity<List<TaskListTemplateModel>> getAllTaskListTemplates() {
    return ResponseEntity.ok(taskListTemplateService.getAllTaskListTemplates());
  }

  @GetMapping("/{active}")
  public ResponseEntity<List<TaskListTemplateModel>> getAllTaskListTemplatesByStatus(@PathVariable boolean active) {
    return ResponseEntity.ok(taskListTemplateService.getAllTaskListTemplatesByActiveStatus(active));
  }

  @PutMapping ResponseEntity<TaskListTemplateModel> updateTaskListTemplate(@RequestBody @Valid TaskListTemplateModel templateModel) {
      return ResponseEntity.ok(taskListTemplateService.updateTaskListTemplate(templateModel));
  }

  @DeleteMapping("/{taskId}")
  ResponseEntity<TaskListTemplateModel> deleteTaskListTemplate(@PathVariable String taskId) {
      taskListTemplateService.deleteTaskListTemplate(taskId);
      return ResponseEntity.ok(null);
  }
}
