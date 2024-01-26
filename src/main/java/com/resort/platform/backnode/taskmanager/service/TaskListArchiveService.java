package com.resort.platform.backnode.taskmanager.service;

import com.resort.platform.backnode.taskmanager.model.TaskListArchiveModel;
import com.resort.platform.backnode.taskmanager.model.TaskModel;
import com.resort.platform.backnode.taskmanager.model.rest.request.TaskStatusChangeRequest;
import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import com.resort.platform.backnode.taskmanager.repo.TaskListArchiveRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskListArchiveService {

  @Autowired
  private TaskListArchiveRepository taskListArchiveRepository;

  public List<TaskListArchiveModel> getAllUserTasksListByDate(ShortDepartmentModel departmentModel,
      LocalDate localDate) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    LocalTime localTime = LocalTime.now();
    List<TaskListArchiveModel> archiveModelList = taskListArchiveRepository
        .findAllByTaskListDate(
            localDate.toString()).orElseThrow();
    archiveModelList = archiveModelList.stream()
        .filter(ar -> localTime.isAfter(LocalTime.parse(ar.getActiveFrom()))).collect(
            Collectors.toList());
    return archiveModelList.stream()
        .filter(archiveModel -> archiveModel.getDepartments().contains(departmentModel)).collect(
            Collectors.toList());
  }

  public TaskListArchiveModel changeTaskCompletedStatus(
      TaskStatusChangeRequest statusChangeRequest) {
    TaskListArchiveModel archiveModel = taskListArchiveRepository.findById(
        statusChangeRequest.getTaskListId()).orElseThrow();
    TaskModel task = archiveModel.getTasks().stream()
        .filter(tsk -> tsk.getId().equals(statusChangeRequest.getTaskId())).findFirst()
        .orElseThrow();
    int index = archiveModel.getTasks().indexOf(task);
    if (task.isCompleted()) {
      task.setCompletedBy(null);
      task.setCompletionTime(null);
    } else {
      task.setCompletedBy(statusChangeRequest.getUser());
      task.setCompletionTime(LocalDateTime.now());
    }
    archiveModel.getTasks().get(index).setCompleted(!task.isCompleted());
    taskListArchiveRepository.save(archiveModel);
    return archiveModel;
  }
}
