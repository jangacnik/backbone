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

  /**
   * Scheduled that with cronjob that runs every midnight.
   * Generates all task list for the new day, depending of their settings.
   *
   * @throws IOException
   * @throws URISyntaxException
   */
  @Scheduled(cron = "0 0 0 * * *")
  private void generateNewTaskListsForNextDay() throws IOException, URISyntaxException {
    administrationController.getTokenScheduled();
  }

  /**
   * Updates employees from planday data every day at 12h and 24h
   *
   * @throws IOException
   * @throws URISyntaxException
   */
  @Scheduled(cron = "59 59 12,23 * * ?")
  private void updateUsers() throws IOException, URISyntaxException {
    administrationController.updateEmployeesAndDepartmentsFromPlandayRestScheduled();
  }
}
