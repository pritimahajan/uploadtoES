package com.javatab.springelasticdemo.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig
{

    @Bean
    public OpenAPI customOpenAPI() {

        Contact defaultContact = new Contact();
        defaultContact.setName("Jayesh Mahajan");
        defaultContact.setUrl("");

        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Elasticsearch upload service")
                        .contact(defaultContact)
                        .version("1.0.0"));
    }
}
