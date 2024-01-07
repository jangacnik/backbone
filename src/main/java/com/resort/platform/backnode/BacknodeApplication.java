package com.resort.platform.backnode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories()
@EnableScheduling
public class BacknodeApplication {

  public static void main(String[] args) {
    SpringApplication.run(BacknodeApplication.class, args);
  }

}
