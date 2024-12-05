package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.UserRegistrationDTO;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PutMapping("/edit-profile")
    public ResponseEntity<String> editProfile(@Valid @RequestBody UserRegistrationDTO userRegistrationDto,
                                              BindingResult bindingResult) {
        logger.info("Received request to edit profile: {}", userRegistrationDto.getEmail());

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            logger.warn("Validation errors: {}", errors);
            return ResponseEntity.badRequest().body(errors.toString());
        }

        try {
            userService.updateUserProfile(userRegistrationDto);
            logger.info("Profile successfully updated for user: {}", userRegistrationDto.getEmail());
            return ResponseEntity.ok("Profile updated successfully");
        } catch (Exception e) {
            logger.error("Error while updating profile: {}", e.getMessage());
            return handleGenericException(e);
        }
    }

    private ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}