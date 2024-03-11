package com.resort.platform.backnode.taskmanager.repo;

import com.resort.platform.backnode.taskmanager.model.TaskListArchiveModel;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskListArchiveRepository extends MongoRepository<TaskListArchiveModel, String> {
  Optional<List<TaskListArchiveModel>> findAllByTaskListDate(@NotNull String taskListDate);
}
