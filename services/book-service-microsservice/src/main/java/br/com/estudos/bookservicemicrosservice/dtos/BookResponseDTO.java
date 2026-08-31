package br.com.estudos.bookservicemicrosservice.dtos;

public record BookResponseDTO(
        Long id,
        String title,
        String author,
        String launchDate,
        Double price,
        String currency,
        String environment
) {
}
