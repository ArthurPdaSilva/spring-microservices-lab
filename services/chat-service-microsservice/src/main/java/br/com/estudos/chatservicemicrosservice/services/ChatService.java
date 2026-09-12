package br.com.estudos.chatservicemicrosservice.services;

import br.com.estudos.chatservicemicrosservice.models.ChatRequest;
import br.com.estudos.chatservicemicrosservice.models.ChatResponse;
import br.com.estudos.chatservicemicrosservice.models.OpenAIRequest;
import br.com.estudos.chatservicemicrosservice.models.OpenAIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String model;

    public ChatService(
            RestTemplate restTemplate,
            @Value("${openai.api.url}") String apiUrl,
            @Value("${openai.api.model}") String model
    ) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.model = model;
    }

    public ChatResponse createResponse(ChatRequest chatRequest) {
        try {
            var openAIResponse = restTemplate.postForObject(
                    apiUrl,
                    new OpenAIRequest(model, chatRequest.message()),
                    OpenAIResponse.class
            );

            if (openAIResponse == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "OpenAI returned an empty response");
            }

            var answer = extractAnswer(openAIResponse);
            if (answer.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "OpenAI returned no text output");
            }

            return new ChatResponse(openAIResponse.id(), openAIResponse.status(), answer);
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (RestClientException exception) {
            logger.error("OpenAI API request failed", exception);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "OpenAI API request failed", exception);
        }
    }

    private String extractAnswer(OpenAIResponse response) {
        return Objects.requireNonNullElse(response.output(), List.<OpenAIResponse.Output>of()).stream()
                .filter(output -> output.content() != null)
                .flatMap(output -> output.content().stream())
                .filter(content -> "output_text".equals(content.type()))
                .map(OpenAIResponse.Content::text)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
    }
}
