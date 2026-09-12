package br.com.estudos.chatservicemicrosservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAIConfig {
    private final Logger logger = LoggerFactory.getLogger(OpenAIConfig.class);

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Bean
    public RestTemplate restTemplate() {
        logger.info("Configuring RestTemplate for OpenAI API with URL: {}", apiUrl);
        var restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(apiKey);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
