package com.resort.platform.backnode.taskmanager.service;

import com.resort.platform.backnode.taskmanager.model.TaskListArchiveModel;
import com.resort.platform.backnode.taskmanager.model.TaskListTemplateModel;
import com.resort.platform.backnode.taskmanager.model.TaskModel;
import com.resort.platform.backnode.taskmanager.model.enums.RepeatDayEnum;
import com.resort.platform.backnode.taskmanager.model.enums.RepeatEnum;
import com.resort.platform.backnode.taskmanager.repo.TaskListArchiveRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {

  private static String takeoverPostfix = " (Yesterdays incomplete)";
  @Autowired
  private TaskListArchiveService taskListArchiveService;
  @Autowired
  private TaskListTemplateService taskListTemplateService;
  @Autowired
  private TaskListArchiveRepository taskListArchiveRepository;

  public void forceGeneration() {
    this.generateNewTaskListsForNextDay();
    this.generateTaskListWithTakeoverTasks();
  }

  /**
   * Generates new Task List for the next day at midnight each day. Only Task list that should be
   * repeated that day will be generated. To generate a new list the Template of the list has to be
   * set to active. The generated task list will only include task that are active
   */
  @Scheduled(cron = "0 0 0 * * *")
  private void generateNewTaskListsForNextDay() {
    List<TaskListTemplateModel> templateModelList =
        taskListTemplateService.getAllTaskListTemplatesByActiveStatus(true);
    Calendar calendar = Calendar.getInstance();
    Date newDate = new Date();
    calendar.setTime(newDate);
    LocalDate localDate = LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    DayOfWeek dayOfWeek = DayOfWeek.of(calendar.get(Calendar.DAY_OF_WEEK));
    Integer dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    YearMonth yearMonth = YearMonth.now();
    // kateri ponedeljek v mescu je npr.
    Integer dayInWeekOfMonth = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    List<TaskListTemplateModel> filteredActiveButNoRepeat = templateModelList.stream().filter(tpm -> tpm.getRepeat().getRepeatType().equals(RepeatEnum.NONE)).toList();

    List<TaskListTemplateModel> filteredTemplateModelListWeeklyTask = templateModelList.stream()
        .filter(
            templateModel ->
                (templateModel.getRepeat().getRepeatType().equals(RepeatEnum.WEEKLY)
                    && templateModel.getRepeat().getRepeatOnWeekDays() != null
                    && templateModel.getRepeat().getRepeatOnWeekDays().contains(dayOfWeek))
        ).toList();

    // get task that are repeated on x of month
    List<TaskListTemplateModel> filteredTemplateModelListMonthlyTaskType1 = templateModelList.stream()
        .filter(
            templateModel ->
                (templateModel.getRepeat().getRepeatType().equals(RepeatEnum.MONTHLY)
                    && templateModel.getRepeat().getRepeatOnMonthDay() != null
                    && templateModel.getRepeat().getRepeatOnMonthDay().equals(dayOfMonth))
        ).toList();
    // get task to be completed on each x-th day in month
    List<TaskListTemplateModel> filteredTemplateModelListMonthlyType2 = templateModelList.stream()
        .filter(
            templateModel ->
                templateModel.getRepeat().getRepeatType().equals(RepeatEnum.MONTHLY)
                    &&
                    (
                        (
                            templateModel.getRepeat().getRepeatDayEnum()
                                .equals(RepeatDayEnum.LASTDAY)
                                && dayOfMonth.equals(yearMonth.atEndOfMonth().getDayOfMonth())
                        )
                            ||
                            (
                                templateModel.getRepeat().getRepeatDayEnum()
                                    .equals(RepeatDayEnum.FIRSTDAY)
                                    && dayOfMonth.equals(1)
                            )
                            ||
                            (templateModel.getRepeat().getRepeatDayEnum().toString()
                                .equals(dayOfWeek.toString())
                                && templateModel.getRepeat().getRepeatDayEnumValue()
                                .equals(dayInWeekOfMonth)
                            )

                    )
        ).toList();

    List<TaskListTemplateModel> filteredTemplateModelList = Stream.concat(
        filteredTemplateModelListWeeklyTask.stream(),
        filteredTemplateModelListMonthlyTaskType1.stream()).toList();
    filteredTemplateModelList = Stream.concat(filteredTemplateModelList.stream(),
        filteredTemplateModelListMonthlyType2.stream()).toList();
    filteredTemplateModelList = Stream.concat(filteredTemplateModelList.stream(),
        filteredActiveButNoRepeat.stream()).toList();
    for (TaskListTemplateModel tmpModel : filteredTemplateModelList) {
      TaskListArchiveModel archiveModel = new TaskListArchiveModel();
      archiveModel.setTitle(tmpModel.getTitle());
      archiveModel.setTasks(tmpModel.getTasks().stream().filter(TaskModel::isActive).collect(
          Collectors.toList()));
      for(TaskModel tsk: archiveModel.getTasks()) {
        tsk.setId(UUID.randomUUID().toString());
      }
      archiveModel.setDepartments(tmpModel.getDepartments());
      archiveModel.setTaskListDate(localDate.toString());
      archiveModel.setActiveFrom(tmpModel.getActiveFrom());
      if (!archiveModel.getTasks().isEmpty()) {
        taskListArchiveRepository.save(archiveModel);
      }
    }
    for (TaskListTemplateModel tmpModel: filteredActiveButNoRepeat) {
      tmpModel.setActive(false);
      taskListTemplateService.updateTaskListTemplate(tmpModel);
    }
  }

  /**
   * Cronjob that checks all task of the previous day and takes the ones with the takeover flag
   * which were not completed and adds them to a new list for the next day
   */
  @Scheduled(cron = "0 0 0 * * *")
  private void generateTaskListWithTakeoverTasks() {
    Calendar calendar = Calendar.getInstance();
    Date newDate = new Date();
    calendar.setTime(newDate);
    LocalDate localDate = LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    localDate = localDate.minusDays(1);
    List<TaskListArchiveModel> yesterdaysArchiveTasks = taskListArchiveRepository.findAllByTaskListDate(localDate.toString()).orElseThrow();
    List<TaskListArchiveModel> yesterdaysArchiveTasksWithNotCompletedTask
        = yesterdaysArchiveTasks.stream().filter(
            tasklist -> tasklist.getTasks().stream().anyMatch(task -> !task.isCompleted() && task.isTakeover())
    ).toList();
    for (TaskListArchiveModel tmpModel : yesterdaysArchiveTasksWithNotCompletedTask) {
      TaskListArchiveModel archiveModel = new TaskListArchiveModel();
      archiveModel.setTitle(tmpModel.getTitle() + takeoverPostfix);
      archiveModel.setTasks(tmpModel.getTasks().stream().filter(task -> task.isTakeover() && !task.isCompleted()).collect(
          Collectors.toList()));
      archiveModel.setDepartments(tmpModel.getDepartments());
      archiveModel.setTaskListDate(localDate.toString());
      archiveModel.setActiveFrom(tmpModel.getActiveFrom());
      if(!archiveModel.getTasks().isEmpty()) {
        taskListArchiveRepository.save(archiveModel);
      }
    }
  }
}
