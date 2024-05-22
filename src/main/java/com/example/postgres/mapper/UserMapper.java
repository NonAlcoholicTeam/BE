package com.example.postgres.mapper;

import com.example.postgres.dto.UserDto;
import com.example.postgres.entity.User;

// Employee 엔티티와 EmployeeDto dto를 매핑하는 Mapper 클래스
public class UserMapper {

    // entity -> dto
    public static UserDto mapToEmployeeDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
    }

    // dto -> entity
    public static User mapTOEmployee(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getPassword()
        );
    }
}
