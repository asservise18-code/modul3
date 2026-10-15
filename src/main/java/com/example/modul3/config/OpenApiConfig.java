package com.example.modul3.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Modul3 API",
                description = "API для управления сотрудниками, клиентами, товарами и заказами",
                version = "1.0.0",
                contact = @Contact(
                        name = "Valera",
                        email = "valera@example.com"
                )
        )
)
@SecurityScheme(
        name = "JWT",
        type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}