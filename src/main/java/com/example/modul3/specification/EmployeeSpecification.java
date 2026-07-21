package com.example.modul3.specification;

import com.example.modul3.model.Employee;
import com.example.modul3.model.Role;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasRole(Role role) {
        return (root, query, cb) ->
                cb.equal(root.get("role"), role);
    }

    public static Specification<Employee> firstNameContains(String firstName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("firstName")),
                        "%" + firstName.toLowerCase() + "%");
    }
}
