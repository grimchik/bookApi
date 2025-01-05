package com.example.bookservice.entity;

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
    @Column(name="title")
    private String title;
    @Column(name="genre")
    private String genre;
    @Column(name = "description")
    private String description;
    @Column(name="author")
    private String author;
}

