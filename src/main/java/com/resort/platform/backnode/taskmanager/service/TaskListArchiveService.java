package com.resort.platform.backnode.taskmanager.service;

import com.resort.platform.backnode.taskmanager.model.TaskListArchiveModel;
import com.resort.platform.backnode.taskmanager.model.TaskModel;
import com.resort.platform.backnode.taskmanager.model.rest.request.AssigneeTaskListRequest;
import com.resort.platform.backnode.taskmanager.model.rest.request.TaskCommentRequest;
import com.resort.platform.backnode.taskmanager.model.rest.request.TaskRatingRequest;
import com.resort.platform.backnode.taskmanager.model.rest.request.TaskStatusChangeRequest;
import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import com.resort.platform.backnode.taskmanager.model.util.ShortUserModel;
import com.resort.platform.backnode.taskmanager.model.util.SupervisorCommentModel;
import com.resort.platform.backnode.taskmanager.model.util.SupervisorRatingModel;
import com.resort.platform.backnode.taskmanager.repo.TaskListArchiveRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    LocalDate lc = LocalDate.now();
    if (lc.equals(localDate)) {
      archiveModelList = archiveModelList.stream()
          .filter(ar -> localTime.isAfter(LocalTime.parse(ar.getActiveFrom()))).collect(
              Collectors.toList());
    }
    return archiveModelList.stream()
        .filter(archiveModel -> archiveModel.getDepartments().contains(departmentModel)).collect(
            Collectors.toList());
  }

  public List<TaskListArchiveModel> getAllTaskByDate(LocalDate localDate) {
    return taskListArchiveRepository
        .findAllByTaskListDate(
            localDate.toString()).orElseThrow();
  }

  public void addAssignee(AssigneeTaskListRequest assigneeRequest) {
    TaskListArchiveModel archiveModel = taskListArchiveRepository.findById(
        assigneeRequest.getTaskListId()).orElseThrow();
    TaskModel task = archiveModel.getTasks().stream()
        .filter(tsk -> tsk.getId().equals(assigneeRequest.getTaskId())).findFirst()
        .orElseThrow();
    int index = archiveModel.getTasks().indexOf(task);
    task.setAssignee(assigneeRequest.getAssignee());
    archiveModel.getTasks().set(index,task);
    taskListArchiveRepository.save(archiveModel);
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


  public int migrateTaskModel() {
    List<TaskListArchiveModel> taskListArchiveModelList = taskListArchiveRepository.findAll();
    int taskListsMigrated = 0;
    for (TaskListArchiveModel taskListArchiveModel: taskListArchiveModelList) {
      List<TaskModel> tasks = new ArrayList<>();
      for (TaskModel taskModel: taskListArchiveModel.getTasks()){
        List<ShortUserModel> assignees = new ArrayList<>();
        if (taskModel.getAssignee() != null) {
          assignees.add(taskModel.getAssignee());
        }
        taskModel.setAssignees(assignees);
        taskModel.setAssignee(null);
        tasks.add(taskModel);
      }
      taskListArchiveModel.setTasks(tasks);
      taskListsMigrated++;
      taskListArchiveRepository.save(taskListArchiveModel);
    }
    return taskListsMigrated;
  }

  public TaskModel rateTask(TaskRatingRequest taskRatingRequest) {
    Optional<TaskListArchiveModel> taskListArchiveModelOptional = taskListArchiveRepository.findById(taskRatingRequest.getTaskListId());
    if (taskListArchiveModelOptional.isPresent()) {
      TaskListArchiveModel taskListArchiveModel = taskListArchiveModelOptional.get();
      TaskModel taskModel = taskListArchiveModel.getTasks().stream().filter( task -> taskRatingRequest.getTaskId().equals(task.getId())).findFirst().orElseThrow();
      int index = taskListArchiveModel.getTasks().indexOf(taskModel);
      SupervisorRatingModel supervisorRatingModel = new SupervisorRatingModel(UUID.randomUUID().toString(), taskRatingRequest.getUser(), taskRatingRequest.getRating(), LocalDateTime.now());
      if (taskModel.getSupervisorRatings() != null && !ObjectUtils.isEmpty(taskModel.getSupervisorRatings())) {
        Optional<SupervisorRatingModel> supervisorCommentModelOptional = taskModel.getSupervisorRatings().stream().filter((tsk) -> tsk.getSupervisor().getUserId().equals(taskRatingRequest.getUser().getUserId())).findFirst();
        if (supervisorCommentModelOptional.isPresent()) {
          supervisorRatingModel = supervisorCommentModelOptional.get();
          int sIndex = taskModel.getSupervisorRatings().indexOf(supervisorRatingModel);
          supervisorRatingModel.setCommentTime(LocalDateTime.now());
          supervisorRatingModel.setRating(taskRatingRequest.getRating());
          taskModel.getSupervisorRatings().set(sIndex, supervisorRatingModel);
        } else {
          taskModel.getSupervisorRatings().add(supervisorRatingModel);
        }
      } else {
        List<SupervisorRatingModel> supervisorRatingModelList = new ArrayList<>();
        supervisorRatingModelList.add(supervisorRatingModel);
        taskModel.setSupervisorRatings(supervisorRatingModelList);
      }
      taskListArchiveModel.getTasks().set(index, taskModel);
      taskListArchiveRepository.save(taskListArchiveModel);
      return taskModel;
    }
    return null;
  }

  public TaskModel commentTask(TaskCommentRequest taskCommentRequest) {
    Optional<TaskListArchiveModel> taskListArchiveModelOptional = taskListArchiveRepository.findById(taskCommentRequest.getTaskListId());
    if (taskListArchiveModelOptional.isPresent()) {
      TaskListArchiveModel taskListArchiveModel = taskListArchiveModelOptional.get();
      TaskModel taskModel = taskListArchiveModel.getTasks().stream().filter( task -> taskCommentRequest.getTaskId().equals(task.getId())).findFirst().orElseThrow();
      int index = taskListArchiveModel.getTasks().indexOf(taskModel);
      if (taskModel.getSupervisorComments() != null && !ObjectUtils.isEmpty(taskModel.getSupervisorComments())) {
        Optional<SupervisorCommentModel> supervisorCommentModelOptional = taskModel.getSupervisorComments().stream().filter((tsk) -> tsk.getSupervisor().getUserId().equals(taskCommentRequest.getUser().getUserId())).findFirst();
        if (supervisorCommentModelOptional.isPresent()) {
          SupervisorCommentModel supervisorCommentModel = supervisorCommentModelOptional.get();
          int sIndex = taskModel.getSupervisorComments().indexOf(supervisorCommentModel);
          supervisorCommentModel.setCommentTime(LocalDateTime.now());
          supervisorCommentModel.setSupervisorComments(taskCommentRequest.getComment());
          taskModel.getSupervisorComments().set(sIndex, supervisorCommentModel);
        } else {

          SupervisorCommentModel supervisorCommentModel = new SupervisorCommentModel(UUID.randomUUID().toString(), taskCommentRequest.getUser(), taskCommentRequest.getComment(), LocalDateTime.now());
          taskModel.getSupervisorComments().add(supervisorCommentModel);
        }
      } else {
        List<SupervisorCommentModel> supervisorCommentModelList = new ArrayList<>();
        SupervisorCommentModel supervisorCommentModel = new SupervisorCommentModel(UUID.randomUUID().toString(), taskCommentRequest.getUser(), taskCommentRequest.getComment(), LocalDateTime.now());
        supervisorCommentModelList.add(supervisorCommentModel);
        taskModel.setSupervisorComments(supervisorCommentModelList);
      }
      taskListArchiveModel.getTasks().set(index, taskModel);
      taskListArchiveRepository.save(taskListArchiveModel);
      return taskModel;
    }
    return null;
  }
}
