package com.example.modul3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    MANAGER("Менеджер"),
    ADMIN("Администратор");

    private final String displayName;
}
