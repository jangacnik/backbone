package com.resort.platform.backnode.taskmanager.model.rest.request;

import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArchiveTaskListRequest {

  ShortDepartmentModel department;
  LocalDate date;
}
