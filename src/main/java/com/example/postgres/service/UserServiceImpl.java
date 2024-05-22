package com.example.postgres.service;

import com.example.postgres.dto.UserDto;
import com.example.postgres.entity.User;
import com.example.postgres.exception.ResourceNotFoundException;
import com.example.postgres.mapper.UserMapper;
import com.example.postgres.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

        User user = UserMapper.mapTOEmployee(userDto);
        User savedUser = userRepository.save(user);

        return UserMapper.mapToEmployeeDto(savedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "조회하려는 " + userId + "번 ID의 직원이 없습니다.")
                );

        return UserMapper.mapToEmployeeDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::mapToEmployeeDto).toList();
    }

    @Override
    public UserDto updateUser(Long userId, UserDto updatedUser) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("수정하려는 " + userId + "번 ID가 존재하지 않습니다")
        );

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());

        User updatedUserObj = userRepository.save(user);

        return UserMapper.mapToEmployeeDto(updatedUserObj);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("삭제하려는 " + userId + "번 ID가 존재하지 않습니다")
        );

        userRepository.deleteById(userId);
    }
}
