package com.resort.platform.backnode.auth.controller;

import com.resort.platform.backnode.auth.model.rest.request.SignIn;
import com.resort.platform.backnode.auth.model.rest.request.SignUp;
import com.resort.platform.backnode.auth.model.rest.response.JwtResponse;
import com.resort.platform.backnode.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signup(@RequestBody SignUp request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signin(@RequestBody SignIn request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }
}
