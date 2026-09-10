package br.edu.infnet.booknest.emprestimo.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI booknestEmprestimoOpenAPI () {
        return new OpenAPI().info(new Info().title("Booknest Empréstimo API")
                .description("API REST do microsserviço de empréstimo da biblioteca").version("v1"));
    }
}
