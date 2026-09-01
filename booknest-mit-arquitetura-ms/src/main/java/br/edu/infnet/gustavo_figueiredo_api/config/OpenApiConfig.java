package br.edu.infnet.gustavo_figueiredo_api.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaOpenAPI () {
        return new OpenAPI().info(
                new Info().title("Biblioteca API").description("API REST da biblioteca").version("v1"));
    }
}
