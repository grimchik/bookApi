package com.example.bookservice.Entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="book")
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name="isbn", unique = true)
    private String isbn;
    @Column(name="title", unique = true)
    private String title;
    @Column(name="genre")
    private String genre;
    @Column(name = "description")
    private String description;
    @Column(name="author")
    private String author;
}
