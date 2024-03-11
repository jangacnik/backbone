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

  /**
   * Creates new template Task List model and saves it to database.
   *
   * @param templateModel template task list
   */
  public void createTaskListTemplate(TaskListTemplateModel templateModel) {
    for(TaskModel tsk: templateModel.getTasks()) {
      tsk.setId(UUID.randomUUID().toString());
    }
    this.taskListTemplateRepository.save(templateModel);
  }

  /**
   * @return all existing task templates
   */
  public List<TaskListTemplateModel> getAllTaskListTemplates() {
    return taskListTemplateRepository.findAll();
  }

  /**
   * Return Template task list by given id.
   *
   * @param id task list id
   * @return  {@link TaskListTemplateModel} with given id
   */
  public TaskListTemplateModel getTaskListTemplateById(String id) {
    return taskListTemplateRepository.findById(id).orElseThrow();
  }

  /**
   * Returns all active/inactive template task lists
   *
   * @param active active status of the searched models
   * @return list of {@link TaskListTemplateModel}
   */
  public List<TaskListTemplateModel> getAllTaskListTemplatesByActiveStatus(boolean active) {
    return taskListTemplateRepository.findAllTaskListTemplateModelsByActive(active).orElseThrow();
  }

  /**
   * Updates the given template task list.
   *
   * @param templateModel template task list to be updated
   * @return updated {@link TaskListTemplateModel}
   */
  public TaskListTemplateModel updateTaskListTemplate(TaskListTemplateModel templateModel) {
    for (TaskModel tsk : templateModel.getTasks()) {
      if (tsk.getId() == null || tsk.getId().isBlank()) {
        tsk.setId(UUID.randomUUID().toString());
      }
    }
    return taskListTemplateRepository.save(templateModel);
  }

  /**
   * Deletes template task list with the given id.
   *
   * @param taskListId task list id of the template to be deleted.
   */
  public void deleteTaskListTemplate(String taskListId) {
    taskListTemplateRepository.deleteById(taskListId);
  }
}
