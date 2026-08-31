package br.com.estudos.bookservicemicrosservice.controllers;

import br.com.estudos.bookservicemicrosservice.dtos.BookResponseDTO;
import br.com.estudos.bookservicemicrosservice.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book-service")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/{id}/{currency}")
    public BookResponseDTO findBook(
            @PathVariable Long id,
            @PathVariable String currency
    ) {
        return bookService.findBook(id, currency);
    }
}
