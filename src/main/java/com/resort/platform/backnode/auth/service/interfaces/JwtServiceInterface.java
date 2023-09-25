package com.resort.platform.backnode.auth.service.interfaces;

import com.resort.platform.backnode.auth.model.rest.response.JwtResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtServiceInterface {
    String extractUserName(String token);

    JwtResponse generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
