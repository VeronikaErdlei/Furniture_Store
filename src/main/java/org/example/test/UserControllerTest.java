package org.example.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.example.controller.UserController;
import org.example.dto.UserRegistrationDTO;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        UserRegistrationDTO userDto = new UserRegistrationDTO();
        userDto.setName("Test User");
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setPhoneNumber("1234567890");

        doNothing().when(userService).registerUser(any(UserRegistrationDTO.class));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        verify(userService, times(1)).registerUser(any(UserRegistrationDTO.class));
    }

    @Test
    public void testGetUserByEmail_UserExists() throws Exception {

        Long userId = 1L;
        String email = "test@example.com";
        String password = "password";
        UserRole role = UserRole.USER;
        String name = "Test User";
        String phone = "123456789";

        User user = new User(userId, email, password, role, name, phone, null);

        when(userService.findByEmail(email)).thenReturn(user);

        mockMvc.perform(get("/api/users/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));

        verify(userService, times(1)).findByEmail(email);
    }

    @Test
    public void testGetUserByEmail_UserNotFound() throws Exception {
        when(userService.findByEmail("test@example.com")).thenReturn(null);

        mockMvc.perform(get("/api/users/email/test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(userService, times(1)).findByEmail("test@example.com");
    }

    @Test
    public void testUpdateUserProfile() throws Exception {
        UserRegistrationDTO userDto = new UserRegistrationDTO();
        userDto.setEmail("test@example.com");

        doNothing().when(userService).updateUserProfile(any(UserRegistrationDTO.class));

        mockMvc.perform(put("/api/users/edit-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserProfile(any(UserRegistrationDTO.class));
    }

    @Test
    public void testUpdateUserProfile_InvalidData() throws Exception {
        UserRegistrationDTO userDto = new UserRegistrationDTO();

        doThrow(new ConstraintViolationException("Validation failed", null))
                .when(userService).updateUserProfile(any(UserRegistrationDTO.class));

        mockMvc.perform(put("/api/users/edit-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error updating profile"));
    }
}

