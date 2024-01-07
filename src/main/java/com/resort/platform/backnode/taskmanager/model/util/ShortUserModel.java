package com.resort.platform.backnode.taskmanager.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ShortUserModel {

  private String userId;
  private String name;
  private String surname;
}
