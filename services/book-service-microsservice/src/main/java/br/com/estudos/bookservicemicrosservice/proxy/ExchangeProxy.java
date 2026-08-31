package br.com.estudos.bookservicemicrosservice.proxy;

import br.com.estudos.bookservicemicrosservice.dtos.ExchangeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "exchange-service-microsservice", url = "${EXCHANGE_SERVICE_URL:http://localhost:8000}")
public interface ExchangeProxy {
    @GetMapping("/exchange-service")
    public ExchangeResponseDTO getExchange(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to
    );

}
