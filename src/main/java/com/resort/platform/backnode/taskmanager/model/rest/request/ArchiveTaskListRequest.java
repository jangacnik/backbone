package com.resort.platform.backnode.taskmanager.model.rest.request;

import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
@AllArgsConstructor
public class ArchiveTaskListRequest {
  ShortDepartmentModel department;
  LocalDate date;
}
