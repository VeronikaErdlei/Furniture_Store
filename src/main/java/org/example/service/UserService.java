package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.UserRegistrationDTO;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Transactional
@Service


public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public void registerUser(UserRegistrationDTO userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }
        if (userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number is already taken");
        }
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public void updateUserProfile(UserRegistrationDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userDto.getEmail()));
        user.setName(userDto.getUsername());
        user.setPhoneNumber(userDto.getPhoneNumber());
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UsernameNotFoundException("User not found with id " + userId);
        }
        userRepository.deleteById(userId);
    }

    public UserRegistrationDTO toDTO(User user) {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        setUserRegistrationDTOFields(user, userDTO);
        return userDTO;
    }

    private void setUserRegistrationDTOFields(User user, UserRegistrationDTO userDTO) {
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }
}

