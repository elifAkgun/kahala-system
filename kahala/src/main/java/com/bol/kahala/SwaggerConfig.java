package com.bol.kahala;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration class for Swagger API documentation.
 * This class defines the Swagger configuration to generate API documentation
 * for the Kahala game application's RESTful endpoints.
 */
@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    /**
     * Creates a custom OpenAPI configuration for the Kahala Game API.
     *
     * @return The configured OpenAPI instance.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kahala Game API")
                        .version("1.0")
                        .description("API documentation for the Kahala game application." +
                                " Kahala is a traditional African board game also known as Mancala.")
                );
    }
}