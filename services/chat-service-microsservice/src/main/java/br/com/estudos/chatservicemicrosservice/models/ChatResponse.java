package br.com.estudos.chatservicemicrosservice.models;

public record ChatResponse(
        String id,
        String status,
        String answer
) {
}
