package org.example.test;



import org.example.dto.UserRegistrationDTO;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        UserRegistrationDTO userDto = new UserRegistrationDTO();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setPhoneNumber("1234567890");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        userService.registerUser(userDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUserById_UserExists() {
        Long userId = 1L;


        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setPhoneNumber("1234567890");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(userId);

        assertEquals("test@example.com", foundUser.getEmail());
    }


    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testUpdateUserProfile() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setName("OldName");

        UserRegistrationDTO userDto = new UserRegistrationDTO();
        userDto.setEmail("test@example.com");
        userDto.setUsername("NewName");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        userService.updateUserProfile(userDto);

        assertEquals("NewName", user.getName());

        verify(userRepository, times(1)).save(user);
    }


    @Test
    void getUserById() {
    }

    @Test
    void registerUser() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void updateUserProfile() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void checkUserExistsByEmail() {
    }
}
