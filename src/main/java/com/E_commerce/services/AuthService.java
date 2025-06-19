package com.E_commerce.services;

import com.E_commerce.entity.Token;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service // Marks this as a Spring-managed service
public class AuthService {

    private final TokenService tokenService;
    private final UserService userService;

    // Constructor injection (recommended)
    @Autowired
    public AuthService(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Transactional
    public void confirmToken(String token) {
        // 1. Find the token in the database
        Token confirmationToken = tokenService.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        // 2. Check if token is expired
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        // 3. Mark token as confirmed
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        tokenService.save(confirmationToken);

        // 4. Enable the user (or other post-confirmation logic)
        userService.enableUser(confirmationToken.getUser());
    }
}