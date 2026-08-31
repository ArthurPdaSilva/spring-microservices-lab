package br.com.estudos.bookservicemicrosservice.dtos;

import java.math.BigDecimal;

public record ExchangeResponseDTO(
        Long id,
        String from,
        String to,
        BigDecimal conversionFactor,
        Double convertedValue,
        String environment
) {
}
