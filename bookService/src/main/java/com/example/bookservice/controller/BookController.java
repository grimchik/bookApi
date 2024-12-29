package com.example.bookservice.controller;


import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.dto.BookWithoutIdDTO;
import com.example.bookservice.exception.BookValidationException;
import com.example.bookservice.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/all")
    public ResponseEntity<?> getBooks() {
        return ResponseEntity.ok().body(bookService.findAllBooks());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok().body(bookService.findBookById(id));
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok().body(bookService.findBookByIsbn(isbn));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody BookWithoutIdDTO book, @RequestHeader("Authorization") String authorizationHeader) throws BookValidationException {
        String token = authorizationHeader.replace("Bearer ", "");
        return new ResponseEntity<>(bookService.saveBook(book, token), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@Valid @RequestBody BookDTO book, @PathVariable Long id) {
        return new ResponseEntity<>(bookService.updateBook(id, book), HttpStatus.OK);
    }
}


