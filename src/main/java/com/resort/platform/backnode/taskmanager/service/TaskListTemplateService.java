package com.resort.platform.backnode.taskmanager.service;

import com.resort.platform.backnode.foodtracker.service.DepartmentService;
import com.resort.platform.backnode.taskmanager.model.TaskListTemplateModel;
import com.resort.platform.backnode.taskmanager.model.TaskModel;
import com.resort.platform.backnode.taskmanager.repo.TaskListTemplateRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskListTemplateService {

  @Autowired
  private TaskListTemplateRepository taskListTemplateRepository;
  @Autowired
  private DepartmentService departmentService;

  public void createTaskListTemplate(TaskListTemplateModel templateModel) {
    for(TaskModel tsk: templateModel.getTasks()) {
      tsk.setId(UUID.randomUUID().toString());
    }
    this.taskListTemplateRepository.save(templateModel);
  }

  public List<TaskListTemplateModel> getAllTaskListTemplates() {
    return taskListTemplateRepository.findAll();
  }
  public TaskListTemplateModel getTaskListTemplateById(String id) {
    return taskListTemplateRepository.findById(id).orElseThrow();
  }

  public List<TaskListTemplateModel> getAllTaskListTemplatesByActiveStatus(boolean active) {
    return taskListTemplateRepository.findAllTaskListTemplateModelsByActive(active).orElseThrow();
  }

  public TaskListTemplateModel updateTaskListTemplate(TaskListTemplateModel templateModel) {
    for (TaskModel tsk : templateModel.getTasks()) {
      if (tsk.getId() == null || tsk.getId().isBlank()) {
        tsk.setId(UUID.randomUUID().toString());
      }
    }
    return taskListTemplateRepository.save(templateModel);
  }

  public void deleteTaskListTemplate(String taskId) {
    taskListTemplateRepository.deleteById(taskId);
  }
}
