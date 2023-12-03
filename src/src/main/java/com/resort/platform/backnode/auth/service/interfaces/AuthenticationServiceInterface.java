package com.resort.platform.backnode.auth.service.interfaces;

import com.resort.platform.backnode.auth.model.rest.request.SignIn;
import com.resort.platform.backnode.auth.model.rest.request.NewUserRequest;
import com.resort.platform.backnode.auth.model.rest.response.JwtResponse;

public interface AuthenticationServiceInterface {

  JwtResponse signin(SignIn request);

  void addNewUser(NewUserRequest request);
}
