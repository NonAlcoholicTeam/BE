package com.example.mini_project.service;

import com.example.mini_project.dto.UserDto;
import com.example.mini_project.entity.User;
import com.example.mini_project.exception.ResourceNotFoundException;
import com.example.mini_project.mapper.UserMapper;
import com.example.mini_project.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        log.info(userDto.getEmail());
        User user = UserMapper.mapTOEmployee(userDto, passwordEncoder);
        log.info(userDto.getEmail());
        User savedUser = userRepository.save(user);

        return UserMapper.mapToEmployeeDto(savedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "조회하려는 " + userId + "번 ID의 회원이 없습니다.")
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

        user.updateUser(updatedUser);

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
