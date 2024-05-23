package com.example.mini_project.controller;

import com.example.mini_project.dto.UserDto;
import com.example.mini_project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class UserController {

    private final UserService userService;

    // Add Employee
    @PostMapping
    public ResponseEntity<UserDto> createEmployee(@RequestBody UserDto userDto) {
        UserDto savedEmployee = userService.createUser(userDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // Get Employee
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getEmployeeById(@PathVariable Long userId) {
        UserDto employee = userService.getUserById(userId);
        return ResponseEntity.ok(employee);
    }

    // Get All Employees
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllEmployees() {
        List<UserDto> allEmployees = userService.getAllUsers();
        return ResponseEntity.ok(allEmployees);
    }

    // Update Employee
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateEmployee(@PathVariable Long userId,
                                                  @RequestBody UserDto updatedEmployee) {
        UserDto userDto = userService.updateUser(userId, updatedEmployee);
        return ResponseEntity.ok(userDto);
    }

    // Delete Employee
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(userId + "번 ID의 회원 정보가 성공적으로 삭제됐습니다.");
    }
}
