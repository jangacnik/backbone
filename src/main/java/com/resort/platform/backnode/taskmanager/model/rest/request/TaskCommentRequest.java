package com.resort.platform.backnode.taskmanager.model.rest.request;

import com.resort.platform.backnode.taskmanager.model.util.ShortUserModel;
import lombok.Data;

@Data
public class TaskCommentRequest {
  private ShortUserModel user;
  private String taskListId;
  private String taskId;
  private String comment;
}
