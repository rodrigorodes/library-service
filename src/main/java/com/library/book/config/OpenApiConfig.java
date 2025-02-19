package com.library.book.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .components(new Components());
    }

    private Info createApiInfo() {
        return new Info()
                .title("Book API Documentation")
                .version("1.0.0")
                .description("This API provides access to the user-related operations.")
                .extensions(Map.of("x-logo", "https://example.com/logo.png"));
    }
}
