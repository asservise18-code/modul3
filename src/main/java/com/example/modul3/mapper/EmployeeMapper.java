package com.example.modul3.mapper;

import com.example.modul3.dto.EmployeeRequestDto;
import com.example.modul3.dto.EmployeeResponseDto;
import com.example.modul3.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeRequestDto dto);

    EmployeeResponseDto toResponseDto(Employee employee);
}