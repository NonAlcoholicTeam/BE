package com.example.postgres.controller;

import com.example.postgres.dto.EmployeeDto;
import com.example.postgres.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // Add Employee
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // Get Employee
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long employeeId) {
        EmployeeDto employee = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employee);
    }

    // Get All Employees
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> allEmployees = employeeService.getAllEmployees();
        return ResponseEntity.ok(allEmployees);
    }
}
