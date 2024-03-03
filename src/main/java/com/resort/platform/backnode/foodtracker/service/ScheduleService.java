package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.foodtracker.controller.AdministrationController;
import java.io.IOException;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
  @Autowired
  private AdministrationController administrationController;
  @Scheduled(cron = "0 0 0 * * *")
  private void generateNewTaskListsForNextDay() throws IOException, URISyntaxException {
    administrationController.getTokenScheduled();
  }

  @Scheduled(cron = "59 59 12,23 * * ?")
  private void updateUsers() throws IOException, URISyntaxException {
    administrationController.updateEmployeesAndDepartmentsFromPlandayRestScheduled();
  }
}
