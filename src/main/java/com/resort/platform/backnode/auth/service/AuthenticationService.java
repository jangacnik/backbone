package com.resort.platform.backnode.auth.service;

import com.resort.platform.backnode.auth.exceptions.UserAlreadyExistsExceptions;
import com.resort.platform.backnode.auth.model.User;
import com.resort.platform.backnode.auth.model.enums.Role;
import com.resort.platform.backnode.auth.model.rest.request.NewUserRequest;
import com.resort.platform.backnode.auth.model.rest.request.PasswordChangeRequest;
import com.resort.platform.backnode.auth.model.rest.request.SignIn;
import com.resort.platform.backnode.auth.model.rest.response.JwtResponse;
import com.resort.platform.backnode.auth.repo.UserRepository;
import com.resort.platform.backnode.auth.service.interfaces.AuthenticationServiceInterface;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationServiceInterface {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;

  @Override
  public void addNewUser(NewUserRequest request) {
    Optional<User> userOptional = userRepository.findUserByEmail(request.getEmail());
    if (userOptional.isPresent()) {
      throw new UserAlreadyExistsExceptions(
          "User with E-mail: " + request.getEmail() + " already exists");
    }
    String password = request.getPassword();
    if(StringUtils.isBlank(password) || StringUtils.isEmpty(password)) {
      password = request.getFirstName() + request.getEmployeeNumber();
    }
    var user = User.builder()
        .id(request.getId())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .employeeNumber(request.getEmployeeNumber())
        .email(request.getEmail()).password(passwordEncoder.encode(password))
        .roles(request.getRoles() != null ? request.getRoles() : new ArrayList<>()).build();
    userRepository.save(user);
  }

  @Override
  public JwtResponse signin(SignIn request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    var user = userRepository.findUserByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    return jwtService.generateToken(user);
  }

  public boolean changePassword(String token, PasswordChangeRequest passwordChangeRequest) {
    String t = token.substring(7);
    String un = this.jwtService.extractUserName(t);
    var user = userRepository.findUserByEmail(un)
        .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    if (passwordEncoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
      user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
      userRepository.save(user);
      return true;
    }
    return false;
  }

  public JwtResponse signinAdmin(SignIn request) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
    authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    var user = userRepository.findUserByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    if (user.getRoles().contains(Role.ADMIN)) {
      return jwtService.generateToken(user);
    }
    return null;
  }

  public JwtResponse refresh(String token) {
    String refreshToken = token.substring(7);
    String userEmail = jwtService.extractUserName(refreshToken);
    UserDetails userDetails = userService.userDetailsService()
        .loadUserByUsername(userEmail);
    if (jwtService.isTokenValid(refreshToken, userDetails)) {
      return jwtService.generateToken(userDetails);
    }
    throw new UsernameNotFoundException("Not found");
  }
}
