package br.com.estudos.bookservicemicrosservice.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(nullable = false, length = 180)
    private String author;

    @Column(nullable = false)
    private LocalDateTime launchDate;

    @Column(nullable = false)
    private Double price;

    public Book() {
    }

    public Book(Long id, String title, String author, LocalDateTime launchDate, Double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getLaunchDate() {
        return launchDate;
    }

    public Double getPrice() {
        return price;
    }
}
