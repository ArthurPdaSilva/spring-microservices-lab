package br.com.estudos.chatservicemicrosservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url("/chat-service")))
                .info(new Info()
                        .title("Chat Service API")
                        .version("1.0")
                        .description("API for generating responses with OpenAI"));
    }
}
