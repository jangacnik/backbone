package com.resort.platform.backnode.auth.repo;

import com.resort.platform.backnode.auth.model.User;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByEmployeeNumber(String employeeNumber);

  Optional<User> findUserByEmployeeNumberOrEmail(String employeeNumber, String username);

  Optional<User> deleteUserByEmail(String email);

  Optional<User> deleteUserByEmployeeNumber(String employeeNumber);
  Optional<User> deleteUserById(String id);

  Optional<ArrayList<User>> findAllByEmployeeNumberNotIn(ArrayList<String> ids);
}
