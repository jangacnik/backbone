package com.resort.platform.backnode.taskmanager.model.rest.request;

import com.resort.platform.backnode.taskmanager.model.util.ShortUserModel;
import lombok.Data;

@Data
public class TaskRatingRequest {
  private ShortUserModel user;
  private String taskListId;
  private String taskId;
  private int rating;
}
