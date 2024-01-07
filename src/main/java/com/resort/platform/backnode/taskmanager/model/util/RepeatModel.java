package com.resort.platform.backnode.taskmanager.model.util;

import com.resort.platform.backnode.taskmanager.model.enums.RepeatDayEnum;
import com.resort.platform.backnode.taskmanager.model.enums.RepeatEnum;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RepeatModel {

  @NotNull
  RepeatEnum repeatType;
  // repeat every X Weeks / Months
  private Integer repeatEvery;
  // repeat on specific day of week
  private List<DayOfWeek> repeatOnWeekDays;
  private Integer repeatOnMonthDay;
  // repeat monthly on x
  private RepeatDayEnum repeatDayEnum;
  // repeat on every x-th repeatDayEnum
  private Integer repeatDayEnumValue;
}
