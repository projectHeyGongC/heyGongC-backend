package com.heygongc.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        Info info = new Info();
        info.title("HeyGongC Api Documentation").description("어이 공씨 API 문서입니다.").version("1.0.0");

        SecurityScheme securityScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement schemaRequirement = new SecurityRequirement().addList("jwt");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("jwt", securityScheme))
                .addSecurityItem(schemaRequirement)
                .info(info);
    }
}