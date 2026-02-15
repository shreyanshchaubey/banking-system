package com.banking.accounts.controller;

import com.banking.accounts.config.JwtUtil;
import com.banking.accounts.dto.AuthRequest;
import com.banking.accounts.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Auth Controller - Handles JWT token generation for authentication.
 * Provides login endpoint that returns a JWT token for authenticated users.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "JWT Authentication APIs")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /api/auth/login - Authenticate user and generate JWT token.
     * Default credentials: admin / admin123
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with username/password and receive a JWT token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
