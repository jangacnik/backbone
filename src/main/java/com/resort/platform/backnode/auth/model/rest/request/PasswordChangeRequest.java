package com.resort.platform.backnode.auth.model.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

  String oldPassword;
  String newPassword;
  String confirmPassword;
}
