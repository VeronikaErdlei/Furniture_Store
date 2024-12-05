package org.example.mapper;

import org.example.dto.UserRegistrationDTO;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserRegistrationDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        return dto;
    }

    public User toEntity(UserRegistrationDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        return user;
    }
}
