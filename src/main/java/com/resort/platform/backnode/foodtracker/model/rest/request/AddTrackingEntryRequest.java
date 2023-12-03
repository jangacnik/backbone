package com.resort.platform.backnode.foodtracker.model.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AddTrackingEntryRequest {

  @NotBlank
  String employeeNumber;
  @NotBlank
  String qrPasscode;
}
