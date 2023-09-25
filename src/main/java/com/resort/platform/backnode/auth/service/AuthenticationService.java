package com.resort.platform.backnode.auth.service;

import com.resort.platform.backnode.auth.exceptions.UserAlreadyExistsExceptions;
import com.resort.platform.backnode.auth.model.User;
import com.resort.platform.backnode.auth.model.enums.Role;
import com.resort.platform.backnode.auth.model.rest.request.SignIn;
import com.resort.platform.backnode.auth.model.rest.request.NewUserRequest;
import com.resort.platform.backnode.auth.model.rest.response.JwtResponse;
import com.resort.platform.backnode.auth.repo.UserRepository;
import com.resort.platform.backnode.auth.service.interfaces.AuthenticationServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

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
        if(userOptional.isPresent()) {
            throw new UserAlreadyExistsExceptions("User with E-mail: " + request.getEmail() + " already exists");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .employeeNumber(request.getEmployeeNumber())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
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

    public JwtResponse signinAdmin(SignIn request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (user.getRoles().contains(Role.ADMIN)) {
            return jwtService.generateToken(user);
        }
        // TODO replace with exception
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
        throw  new UsernameNotFoundException("Not found");
    }
}
