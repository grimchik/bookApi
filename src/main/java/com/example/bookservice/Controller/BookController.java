package com.example.bookservice.controller;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.dto.BookWithoutIdDTO;
import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.BookValidationException;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired
    private BookService bookService;
    @GetMapping("/all-book")
    public ResponseEntity<?> getBooks() {
        return ResponseEntity.ok().body(bookService.findAllBooks());
    }
    @GetMapping("/book/id/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok().body(bookService.findBookById(id));
    }
    @GetMapping("/book/isbn/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok().body(bookService.findBookByIsbn(isbn));
    }
    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id)
    {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/book")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookWithoutIdDTO book, @RequestHeader("Authorization") String authorizationHeader) throws BookValidationException {
        String token = authorizationHeader.replace("Bearer ", "");
        BookWithoutIdDTO bookWithoutIdDTO = bookService.saveBook(book,token);
        return new ResponseEntity<>(bookWithoutIdDTO,HttpStatus.CREATED);
    }
    @PutMapping("/book/{id}")
    public ResponseEntity<?> updateBook(@Valid @RequestBody BookDTO book, @PathVariable Long id)
    {
        System.out.println("Validated Book DTO: " + book);
        BookDTO updatedBook = bookService.updateBook(id,book);
        return new ResponseEntity<>(updatedBook,HttpStatus.OK);
    }
}
