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
        try {
            return ResponseEntity.ok().body(bookService.findAllBooks());
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/book/id/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try
        {
            return ResponseEntity.ok().body(bookService.findBookById(id));
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/book/isbn/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn) {
        try
        {
            return ResponseEntity.ok().body(bookService.findBookByIsbn(isbn));
        }
        catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id)
    {
        try {
            bookService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        catch (EntityNotFoundException exception)
        {
            return  ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/book")
    public ResponseEntity<?> createBook(@RequestBody BookWithoutIdDTO book, @RequestHeader("Authorization") String authorizationHeader) throws BookValidationException {
        try
        {
            String token = authorizationHeader.replace("Bearer ", "");
            BookWithoutIdDTO bookWithoutIdDTO = bookService.saveBook(book,token);
            return new ResponseEntity<>(bookWithoutIdDTO,HttpStatus.CREATED);
        }
        catch (EntityExistsException e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
        catch (BookValidationException exc)
        {
            return new ResponseEntity<>(exc.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PutMapping("/book/{id}")
    public ResponseEntity<?> updateBook(@RequestBody BookDTO book, @PathVariable Long id)
    {
        try {
            BookDTO updatedBook = bookService.updateBook(id,book);
            return new ResponseEntity<>(updatedBook,HttpStatus.OK);
        } catch (BookValidationException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (EntityNotFoundException exc)
        {
            return new ResponseEntity<>(exc.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch (EntityExistsException e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }
}
