package com.banking.accounts.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application Configuration - Defines beans for ModelMapper, RestTemplate, and Swagger/OpenAPI.
 */
@Configuration
public class AppConfig {

    /**
     * ModelMapper bean for DTO-Entity mapping.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * RestTemplate bean for service-to-service REST communication.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * OpenAPI/Swagger configuration for Accounts Service documentation.
     * Includes JWT Bearer token authentication in Swagger UI.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Accounts Service API")
                        .version("1.0.0")
                        .description("Banking System - Accounts Microservice API Documentation. " +
                                "Manages bank account creation, retrieval, update, and deletion.")
                        .contact(new Contact()
                                .name("Banking System Team")
                                .email("support@banking.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT")
                                        .scheme("bearer")));
    }
}
