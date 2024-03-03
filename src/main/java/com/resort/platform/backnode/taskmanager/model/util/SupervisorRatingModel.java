package com.resort.platform.backnode.taskmanager.model.util;

import com.resort.platform.backnode.taskmanager.model.enums.TaskRating;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorRatingModel {
  private String id;
  private ShortUserModel supervisor;
  private int rating;
  private LocalDateTime commentTime;
}
