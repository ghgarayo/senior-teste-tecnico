package br.com.senior.seniortestetecnico.infra.springdoc;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(
                        new Info()
                                .title("Senior Teste Técnico")
                                .description("API Rest da aplicação Senior Teste Técnico, desenvolvida como parte do processo seletivo da Senior Sistemas.")
                                .version("1.0")
                                .contact(new Contact()
                                                .name("Gustavo Henrique Garayo")
                                                .email("gustavo.garayo@senior.com.br")
                                )
                );
    }

}