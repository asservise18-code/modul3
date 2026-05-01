package com.example.modul3.model;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum Role {
    MANAGER("Менеджер"),
    ADMIN("Администратор");

    private final String displayName;
}
