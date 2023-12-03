package com.resort.platform.backnode.auth.controller;

import com.resort.platform.backnode.auth.model.rest.request.SignIn;
import com.resort.platform.backnode.auth.model.rest.request.NewUserRequest;
import com.resort.platform.backnode.auth.model.rest.response.JwtResponse;
import com.resort.platform.backnode.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/new")
  public ResponseEntity<Void> addNewUser(@RequestBody NewUserRequest request) {
    authenticationService.addNewUser(request);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/signin")
  @CrossOrigin(origins = "http://localhost:4200")
  public ResponseEntity<JwtResponse> signin(@RequestBody SignIn request) {
    return ResponseEntity.ok(authenticationService.signin(request));
  }
}
