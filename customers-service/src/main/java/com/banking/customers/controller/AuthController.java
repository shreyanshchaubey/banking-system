package com.banking.customers.controller;

import com.banking.customers.config.JwtUtil;
import com.banking.customers.dto.AuthRequest;
import com.banking.customers.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Auth Controller - JWT authentication for Customers Service.
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

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate and receive JWT token. Default: admin/admin123")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
