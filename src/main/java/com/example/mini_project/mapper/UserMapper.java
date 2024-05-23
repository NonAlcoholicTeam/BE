package com.example.mini_project.mapper;

import com.example.mini_project.dto.UserDto;
import com.example.mini_project.entity.User;
import com.example.mini_project.entity.UserRoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;

// Employee 엔티티와 EmployeeDto dto를 매핑하는 Mapper 클래스
public class UserMapper {

    // entity -> dto
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    // dto -> entity
    // Role : User
    public static User mapToUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        return new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                UserRoleEnum.USER);
    }

    // Role : Admin
    public static User mapToAdmin(UserDto userDto, PasswordEncoder passwordEncoder) {
        return new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                UserRoleEnum.ADMIN);
    }
}
