package com.example.mini_project.mapper;

import com.example.mini_project.dto.UserDto;
import com.example.mini_project.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

// Employee 엔티티와 EmployeeDto dto를 매핑하는 Mapper 클래스
public class UserMapper {

    // entity -> dto
    public static UserDto mapToEmployeeDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    // dto -> entity
    public static User mapTOEmployee(UserDto userDto, PasswordEncoder passwordEncoder) {
        return new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword())
        );
    }
}
