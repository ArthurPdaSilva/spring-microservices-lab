package br.com.estudos.bookservicemicrosservice.controllers;

import br.com.estudos.bookservicemicrosservice.dtos.BookResponseDTO;
import br.com.estudos.bookservicemicrosservice.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book-service")
@Tag(name = "Book Service API", description = "API for managing books and their prices in different currencies")
public class BookController {
    @Autowired
    private BookService bookService;

    @Operation(summary = "Find a book by ID and convert its price to the specified currency")
    @GetMapping("/{id}/{currency}")
    public BookResponseDTO findBook(
            @PathVariable Long id,
            @PathVariable String currency
    ) {
        return bookService.findBook(id, currency);
    }
}
