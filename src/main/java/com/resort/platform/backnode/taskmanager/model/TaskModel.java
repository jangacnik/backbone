package com.resort.platform.backnode.taskmanager.model;

import com.resort.platform.backnode.taskmanager.model.util.ShortUserModel;
import com.resort.platform.backnode.taskmanager.model.util.SupervisorCommentModel;
import com.resort.platform.backnode.taskmanager.model.util.SupervisorRatingModel;
import com.resort.platform.backnode.taskmanager.model.util.SupervisorSignedModel;
import com.resort.platform.backnode.taskmanager.model.util.TaskCommentModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TaskModel {

  @Id
  private String id;
  @NotBlank
  private String title;
  private String description;
  private ShortUserModel assignee;
  private List<ShortUserModel> assignees;
  private ShortUserModel completedBy;
  private List<SupervisorRatingModel> supervisorRatings;
  private List<SupervisorCommentModel> supervisorComments;
  @NotNull
  private LocalDateTime creationTime;
  private LocalDateTime completionTime;
  private LocalTime dueTime;
  @NotNull
  private boolean takeover;
  @NotNull
  private boolean completed;
  @NotNull
  private boolean active;
  private boolean evaluate;
  private List<TaskCommentModel> comments;
}
