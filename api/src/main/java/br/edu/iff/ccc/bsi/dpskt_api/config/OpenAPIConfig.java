package br.edu.iff.ccc.bsi.dpskt_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dpskt Api")
                        .version("1.0.0")
                        .description("Documentação da API para controle de ponto")
                        .contact(new Contact()
                                .name("Bianca Medeiros")
                                .url("https://github.com/bkkater")));
    }
}
