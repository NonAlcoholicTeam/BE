package com.example.postgres.service;

import com.example.postgres.dto.EmployeeDto;
import com.example.postgres.entity.Employee;
import com.example.postgres.mapper.EmployeeMapper;
import com.example.postgres.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return null;
    }
}
