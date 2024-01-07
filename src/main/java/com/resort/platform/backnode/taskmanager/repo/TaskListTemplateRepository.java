package com.resort.platform.backnode.taskmanager.repo;

import com.resort.platform.backnode.taskmanager.model.TaskListArchiveModel;
import com.resort.platform.backnode.taskmanager.model.TaskListTemplateModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskListTemplateRepository extends MongoRepository<TaskListTemplateModel, String> {
  Optional<List<TaskListTemplateModel>> findAllTaskListTemplateModelsByActive(boolean active);
}
