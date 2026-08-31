package br.com.estudos.exchangeservicemicrosservice.dtos;

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
