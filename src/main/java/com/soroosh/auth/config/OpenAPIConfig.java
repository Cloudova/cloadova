package com.soroosh.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openapi = new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer").bearerFormat("JWT"))
                        .addSecuritySchemes("app-id",
                                new SecurityScheme().
                                        type(SecurityScheme.Type.APIKEY).scheme("uuid")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("app-id")
                        ));
        openapi.setInfo(new Info().title("Cloudova Application Service").version("0.1.0-Beta"));
        return openapi;
    }
}
