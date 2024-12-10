package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.dto.UserRegistrationDTO;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Handles the registration of a new user. Validates the user input and creates the user if successful."
    )
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDto,
                                               BindingResult bindingResult) {
        logger.info("Received request for user registration: {}", userRegistrationDto.getEmail());

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            logger.warn("Validation errors during registration: {}", errors);
            return ResponseEntity.badRequest().body(errors.toString());
        }

        try {
            userService.registerUser(userRegistrationDto);
            logger.info("User successfully registered: {}", userRegistrationDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage());
            return handleGenericException(e);
        }
    }

    @Operation(
            summary = "Delete a user",
            description = "Handles the deletion of a user by their ID."
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        logger.info("Received request to delete user with ID: {}", userId);

        try {
            userService.deleteUser(userId);
            logger.info("User successfully deleted with ID: {}", userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            logger.error("Error during user deletion: {}", e.getMessage());
            return handleGenericException(e);
        }
    }

    private ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }

}
