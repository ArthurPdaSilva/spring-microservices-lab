package br.com.estudos.chatservicemicrosservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAIResponse(
        String id,
        String status,
        List<Output> output
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Output(
            String type,
            List<Content> content
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Content(
            String type,
            String text
    ) {
    }
}
