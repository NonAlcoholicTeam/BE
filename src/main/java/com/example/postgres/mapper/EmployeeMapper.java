package com.example.postgres.mapper;

import com.example.postgres.dto.EmployeeDto;
import com.example.postgres.entity.Employee;

// Employee 엔티티와 EmployeeDto dto를 매핑하는 Mapper 클래스
public class EmployeeMapper {

    // entity -> dto
    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );
    }


}
