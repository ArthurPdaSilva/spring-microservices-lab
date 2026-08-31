package br.com.estudos.exchangeservicemicrosservice.controllers;

import br.com.estudos.exchangeservicemicrosservice.dtos.ExchangeResponseDTO;
import br.com.estudos.exchangeservicemicrosservice.services.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/exchange-service")
public class ExchangeController {
    @Autowired
    private ExchangeService exchangeService;

    @GetMapping
    public ExchangeResponseDTO getExchange(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to
    ) {
        return exchangeService.getExchange(amount, from, to);
    }

}
