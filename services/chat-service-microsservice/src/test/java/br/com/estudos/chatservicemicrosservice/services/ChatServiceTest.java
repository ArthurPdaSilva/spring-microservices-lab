package br.com.estudos.chatservicemicrosservice.services;

import br.com.estudos.chatservicemicrosservice.models.ChatRequest;
import br.com.estudos.chatservicemicrosservice.models.OpenAIResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatServiceTest {
    @Test
    void shouldReturnTextFromOpenAIResponse() {
        var restTemplate = mock(RestTemplate.class);
        var openAIResponse = new OpenAIResponse(
                "resp_123",
                "completed",
                List.of(new OpenAIResponse.Output(
                        "message",
                        List.of(new OpenAIResponse.Content("output_text", "Hello!"))
                ))
        );

        when(restTemplate.postForObject(
                eq("https://api.openai.com/v1/responses"),
                any(),
                eq(OpenAIResponse.class)
        )).thenReturn(openAIResponse);

        var service = new ChatService(restTemplate, "https://api.openai.com/v1/responses", "gpt-4.1-mini");

        var response = service.createResponse(new ChatRequest("Hi"));

        assertThat(response.id()).isEqualTo("resp_123");
        assertThat(response.status()).isEqualTo("completed");
        assertThat(response.answer()).isEqualTo("Hello!");
    }
}
