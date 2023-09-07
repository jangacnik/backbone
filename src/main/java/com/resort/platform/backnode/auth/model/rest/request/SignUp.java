package com.resort.platform.backnode.auth.model.rest.request;

import com.resort.platform.backnode.auth.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUp {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Role> roles;
}