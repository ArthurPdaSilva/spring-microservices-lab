package br.com.estudos.bookservicemicrosservice.services;

import br.com.estudos.bookservicemicrosservice.dtos.BookResponseDTO;
import br.com.estudos.bookservicemicrosservice.dtos.ExchangeResponseDTO;
import br.com.estudos.bookservicemicrosservice.enviromnent.InstanceInformationService;
import br.com.estudos.bookservicemicrosservice.models.Book;
import br.com.estudos.bookservicemicrosservice.proxy.ExchangeProxy;
import br.com.estudos.bookservicemicrosservice.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class BookService {

    private final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private InstanceInformationService instanceInformationService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ExchangeProxy exchangeProxy;

    public BookResponseDTO findBook(Long id, String currency) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        logger.info("Request to find book with ID: {} and convert price to currency: {}", id, currency);

        ExchangeResponseDTO exchangeResponse = exchangeProxy.getExchange(
                BigDecimal.valueOf(book.getPrice()),
                "USD",
                currency
        );

        if (exchangeResponse == null) {
            throw new RuntimeException("Exchange service not available");
        }

        var port = instanceInformationService.getPort();
        var hostName = instanceInformationService.getHostName();

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getLaunchDate().toString(),
                exchangeResponse.convertedValue(),
                currency,
                "Book Service | HOST: " + hostName
                        + " | PORT: " + port
                        + " | EXCHANGE SERVICE: " + exchangeResponse.environment()
        );
    }
}
