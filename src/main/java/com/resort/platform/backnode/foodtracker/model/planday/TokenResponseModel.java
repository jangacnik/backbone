package com.resort.platform.backnode.foodtracker.model.planday;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TokenResponseModel {
    String id_token;
    String access_token;
    long expires_in;
    String token_type;
    String refresh_token;
    String scope;
}
