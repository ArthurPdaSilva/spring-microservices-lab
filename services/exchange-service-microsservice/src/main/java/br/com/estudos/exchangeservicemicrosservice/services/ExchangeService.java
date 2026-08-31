package br.com.estudos.exchangeservicemicrosservice.services;

import br.com.estudos.exchangeservicemicrosservice.dtos.ExchangeResponseDTO;
import br.com.estudos.exchangeservicemicrosservice.enviromnent.InstanceInformationService;
import br.com.estudos.exchangeservicemicrosservice.repositories.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeService {
    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private InstanceInformationService instanceInformationService;

    public ExchangeResponseDTO getExchange(BigDecimal amount, String from, String to) {
        if (amount == null || from == null || to == null) {
            throw new RuntimeException("Currency Unsupported");
        }

        var exchange = exchangeRepository.findByFromAndTo(from, to);
        if (exchange == null) {
            throw new RuntimeException("Exchange not found for " + from + " to " + to);
        }

        return new ExchangeResponseDTO(
                exchange.getId(),
                exchange.getFrom(),
                exchange.getTo(),
                exchange.getConversionFactor(),
                amount.multiply(exchange.getConversionFactor()).doubleValue(),
                String.valueOf(instanceInformationService.getPort())
        );
    }
}
