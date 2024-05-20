package com.example.postgres.service;

import com.example.postgres.dto.EmployeeDto;
import com.example.postgres.entity.Employee;
import com.example.postgres.exception.ResourceNotFoundException;
import com.example.postgres.mapper.EmployeeMapper;
import com.example.postgres.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapTOEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "조회하려는 " + employeeId + "번 ID의 직원이 없습니다.")
                );

        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(EmployeeMapper::mapToEmployeeDto).toList();
    }
}
