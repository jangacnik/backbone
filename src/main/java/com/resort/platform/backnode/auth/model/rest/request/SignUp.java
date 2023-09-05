package com.resort.platform.backnode.auth.model.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUp {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}