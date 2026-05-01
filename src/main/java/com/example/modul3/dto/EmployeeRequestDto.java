package com.example.modul3.dto;

import com.example.modul3.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
