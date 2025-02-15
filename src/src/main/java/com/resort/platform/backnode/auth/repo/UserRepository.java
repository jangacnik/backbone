package com.resort.platform.backnode.auth.repo;

import com.resort.platform.backnode.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByEmailOrEmployeeNumber(String email, String employeeNumber);

  Optional<User> findUserByEmployeeNumber(String employeeNumber);

  Optional<User> deleteUserByEmail(String email);
}
