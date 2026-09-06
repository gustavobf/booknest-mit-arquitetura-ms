package br.edu.infnet.gustavo_figueiredo_api.config;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.integration.client.*;
import com.fasterxml.jackson.databind.*;
import feign.*;
import feign.jackson.*;
import org.springframework.context.annotation.*;

@Configuration
public class FeignConfig {
    @Bean
    public ViaCepClient viaCepClient () {
        return Feign.builder().decoder(new JacksonDecoder(new ObjectMapper()))
                .target(ViaCepClient.class, "https://viacep.com.br");
    }
}
