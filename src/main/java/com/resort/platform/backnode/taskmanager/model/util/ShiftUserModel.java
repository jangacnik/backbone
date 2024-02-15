package com.resort.platform.backnode.taskmanager.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ShiftUserModel {
  private ShortUserModel user;
  String startTime;
  String endTime;
}
