package com.aronJourney.focus_forge.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Focus Forge API",
                version = "1.0",
                description = "AI-powered collaborative study platform"
        )
)
public class AppConfig {
    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
        @Bean
        public OpenAPI openAPI() {
            return new OpenAPI()
                    .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                    .components(new Components()
                            .addSecuritySchemes("BearerAuth",
                                    new SecurityScheme()
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")));
        }
    }

