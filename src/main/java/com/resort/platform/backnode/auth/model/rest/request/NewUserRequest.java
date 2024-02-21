package com.resort.platform.backnode.auth.model.rest.request;

import com.resort.platform.backnode.auth.model.enums.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
  @Id
  private String id;
  private String firstName;
  private String lastName;
  private String employeeNumber;
  private String email;
  private String password;
  private List<Role> roles;
}