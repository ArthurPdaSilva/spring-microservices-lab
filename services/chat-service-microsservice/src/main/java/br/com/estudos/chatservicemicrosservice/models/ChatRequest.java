package br.com.estudos.chatservicemicrosservice.models;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "Message is required") String message
) {
}
