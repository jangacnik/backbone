package com.resort.platform.backnode.taskmanager.model.rest.request;

import com.resort.platform.backnode.taskmanager.model.util.ShortUserModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskStatusChangeRequest {
  private ShortUserModel user;
  private String taskListId;
  private String taskId;
}
