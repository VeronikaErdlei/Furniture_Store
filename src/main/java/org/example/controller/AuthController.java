package org.example.controller;

import lombok.Data;
import org.example.service.JwtService;
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

        @Autowired
        public AuthController(JwtService jwtService) {
            this.jwtService = jwtService;
        }

        @Data
        public static class AuthRequest {
            private String username;
            private String password;
        }

        @PostMapping("/login")
        public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
            String token = jwtService.generateToken(authRequest.getUsername());

            System.out.println("Generated JWT Token: " + token);

            return ResponseEntity.ok("Bearer " + token);
        }


        @PostMapping("/login-with-credentials")
        public ResponseEntity<Map<String, String>> loginWithCredentials(@RequestBody AuthRequest authRequest) {

            boolean isValid = authenticate(authRequest.getUsername(), authRequest.getPassword());

            if (!isValid) {
                return ResponseEntity.status
                        (HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
            }

            String token = jwtService.generateToken(authRequest.getUsername());

            System.out.println("Generated JWT Token: " + token);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        }

        private boolean authenticate(String username, String password) {

            return username.equals("anna1") && password.equals("password");
        }
    }

