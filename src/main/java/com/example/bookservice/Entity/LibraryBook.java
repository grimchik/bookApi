package com.example.bookservice.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="librarybook")
public class LibraryBook {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private Long idBook;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnBy;
}
