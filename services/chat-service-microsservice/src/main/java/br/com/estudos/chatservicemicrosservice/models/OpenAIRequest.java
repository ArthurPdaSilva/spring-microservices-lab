package br.com.estudos.chatservicemicrosservice.models;

public record OpenAIRequest(
        String model,
        String input
) {
}
