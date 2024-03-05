package com.resort.platform.backnode.taskmanager.model.util;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupervisorCommentModel {

  private String id;
  private ShortUserModel supervisor;
  private String supervisorComments;
  private LocalDateTime commentTime;
}
