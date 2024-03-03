package com.resort.platform.backnode.taskmanager.model.util;

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
