package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.Data;
import org.example.dto.UserRegistrationDTO;
import org.example.service.JwtService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AuthController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Data
    public static class AuthRequest {
        private String username;
        private String password;
    }

    @Operation(summary = "Login and generate JWT token", description = "Generates a JWT token for the user based on their username")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        String token = jwtService.generateToken(authRequest.getUsername());
        System.out.println("Generated JWT Token: " + token);
        return ResponseEntity.ok("Bearer " + token);
    }

    @Operation(summary = "Login with credentials and generate JWT token", description = "Validates the user's credentials and generates a JWT token")
    @PostMapping("/login-with-credentials")
    public ResponseEntity<Map<String, String>> loginWithCredentials(@RequestBody AuthRequest authRequest) {
        boolean isValid = authenticate(authRequest.getUsername(), authRequest.getPassword());
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
        }

        String token = jwtService.generateToken(authRequest.getUsername());
        System.out.println("Generated JWT Token: " + token);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Register a new user", description = "Registers a new user with provided registration details")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationDTO userDto) {
        userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
    }

    private boolean authenticate(String username, String password) {
        return username.equals("anna1") && password.equals("password");
    }
}

