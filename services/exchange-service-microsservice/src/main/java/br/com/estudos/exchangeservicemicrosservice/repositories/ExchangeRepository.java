package br.com.estudos.exchangeservicemicrosservice.repositories;

import br.com.estudos.exchangeservicemicrosservice.models.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    public Exchange findByFromAndTo(String from, String to);
}
