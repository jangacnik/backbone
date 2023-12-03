package com.resort.platform.backnode.foodtracker.model.planday;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRequest {

  String client_id;
  String grant_type;

  String refresh_token;
}
