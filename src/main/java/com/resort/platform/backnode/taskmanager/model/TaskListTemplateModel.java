package com.resort.platform.backnode.taskmanager.model;


import com.resort.platform.backnode.taskmanager.model.util.RepeatModel;
import com.resort.platform.backnode.taskmanager.model.util.ShortDepartmentModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("taskListTemplates")
@AllArgsConstructor
@RequiredArgsConstructor
public class TaskListTemplateModel {

  @Id
  private String id;
  @NotBlank
  private String title;
  @NotNull
  private List<ShortDepartmentModel> departments;
  @NotNull
  private List<TaskModel> tasks;
  private LocalDateTime publishDate;
  @NotNull
  private RepeatModel repeat;
  @NotNull
  private boolean active;
  @NotNull
  private String activeFrom;
}
