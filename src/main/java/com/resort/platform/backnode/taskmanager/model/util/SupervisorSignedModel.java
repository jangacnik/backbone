package com.resort.platform.backnode.taskmanager.model.util;

import com.resort.platform.backnode.taskmanager.model.enums.TaskRating;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupervisorSignedModel {
  private String id;
  private boolean signedBySupervisor;
  private ShortUserModel supervisor;
  private LocalDateTime commentTime;
}
