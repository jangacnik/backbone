package com.resort.platform.backnode.taskmanager.model;


import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("taskListArchive")
@AllArgsConstructor
@RequiredArgsConstructor
public class TaskListArchiveModel {
  @Id
  private String id;
  @NotBlank
  private String title;
  @NotNull
  private List<ShortDepartmentModel> departments;
  @NotNull
  private List<TaskModel> tasks;
  @NotNull
  private LocalDate taskListDate;

}
