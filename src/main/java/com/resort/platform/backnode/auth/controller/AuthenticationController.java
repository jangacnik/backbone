package com.resort.platform.backnode.auth.controller;

import com.resort.platform.backnode.auth.model.rest.request.NewUserRequest;
import com.resort.platform.backnode.auth.model.rest.request.PasswordChangeRequest;
import com.resort.platform.backnode.auth.model.rest.request.SignIn;
import com.resort.platform.backnode.auth.model.rest.response.JwtResponse;
import com.resort.platform.backnode.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

  /**
   * For adding new users to the database, only accessible to ADMIN users
   *
   * @param request - object of new user to be added to database
   * @return - HTTP.OK if successful else throws exception
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/new")
  public ResponseEntity<Void> addNewUser(@RequestBody NewUserRequest request) {
    authenticationService.addNewUser(request);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Sign in endpoint for all users
   *
   * @param request - email and password object
   * @return - jwt token
   */
  @PostMapping("/signin")
  public ResponseEntity<JwtResponse> signin(@RequestBody SignIn request) {
    return ResponseEntity.ok(authenticationService.signin(request));
  }

  /**
   * Sign in endpoint for ADMIN users, will only return jwt if user has role ADMIN
   *
   * @param request - email and password object
   * @return - jwt token
   */
  @PostMapping("/signinadmin")
  public ResponseEntity<JwtResponse> signinAdmin(@RequestBody SignIn request) {
    return ResponseEntity.ok(authenticationService.signinAdmin(request));
  }

  /**
   * Refresh token endpont
   *
   * @param request - refresh Token
   * @return - new jwt token
   */
  @PostMapping("/refresh")
  public ResponseEntity<JwtResponse> refresh(
      @RequestHeader(value = "refreshToken") String request) {
    return ResponseEntity.ok(authenticationService.refresh(request));
  }

  /**
   * @param request               - jwt token of user
   * @param passwordChangeRequest - password change object
   * @return true or false if successful or not
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/password")
  public ResponseEntity<Boolean> changePassword(
      @RequestHeader(value = "Authorization") String request,
      @RequestBody PasswordChangeRequest passwordChangeRequest) {
    return ResponseEntity.ok(authenticationService.changePassword(request, passwordChangeRequest));
  }
}
