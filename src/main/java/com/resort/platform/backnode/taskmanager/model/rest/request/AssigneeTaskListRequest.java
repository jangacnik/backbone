package com.resort.platform.backnode.taskmanager.model.rest.request;

import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import com.resort.platform.backnode.taskmanager.model.util.ShortUserModel;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssigneeTaskListRequest {
  ShortUserModel assignee;
  String taskId;
  String taskListId;
}
