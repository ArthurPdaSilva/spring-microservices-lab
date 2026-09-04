package br.com.estudos.exchangeservicemicrosservice.controllers;

import br.com.estudos.exchangeservicemicrosservice.dtos.ExchangeResponseDTO;
import br.com.estudos.exchangeservicemicrosservice.services.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/exchange-service")
@Tag(name = "Exchange Service API", description = "API for managing currency exchange rates and conversions")
public class ExchangeController {
    @Autowired
    private ExchangeService exchangeService;

    @Operation(summary = "Get exchange rate and converted amount", description = "Returns the exchange rate and converted amount for the specified currencies and amount")
    @GetMapping
    public ExchangeResponseDTO getExchange(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to
    ) {
        return exchangeService.getExchange(amount, from, to);
    }

}
