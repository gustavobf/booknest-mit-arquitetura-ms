package br.edu.infnet.gustavo_figueiredo_api.config;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.integration.client.*;
import br.edu.infnet.gustavo_figueiredo_api.emprestimo.integration.client.*;
import com.fasterxml.jackson.databind.*;
import feign.*;
import feign.jackson.*;
import feign.okhttp.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

@Configuration
public class FeignConfig {

    @Value("${servico.emprestimo.url}")
    private String emprestimoServiceUrl;

    @Bean
    public ViaCepClient viaCepClient () {
        return Feign.builder().decoder(new JacksonDecoder(new ObjectMapper()))
                .target(ViaCepClient.class, "https://viacep.com.br");
    }

    @Bean
    public EmprestimoClient emprestimoClient () {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return Feign.builder().client(new OkHttpClient()).decoder(new JacksonDecoder(objectMapper))
                .encoder(new JacksonEncoder(objectMapper)).target(EmprestimoClient.class, emprestimoServiceUrl);
    }
}
