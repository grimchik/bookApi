package com.example.bookservice.Controller;

import com.example.bookservice.Entity.Book;
import com.example.bookservice.Exception.BookValidationException;
import com.example.bookservice.Repository.BookRepository;
import com.example.bookservice.Service.BookService;
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
public class BookController {
    @Autowired
    private BookService bookService;
    @GetMapping("/api/books")
    public ResponseEntity<List<Book>> getBooks() {
        try {
            return ResponseEntity.ok().body(bookService.findAllBooks());
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/api/book/id/{id}")
    public ResponseEntity<Book> getBooksById(@PathVariable Long id) {
        Optional<Book> book=bookService.findBookById(id);
        if (book.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok().body(book.get());
        }
    }
    @GetMapping("/api/book/isbn/{isbn}")
    public ResponseEntity<?> getBooksByIsbn(@PathVariable String isbn) {
        Optional<Book> book=bookService.findBookByIsbn(isbn);
        if (book.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok().body(book.get());
        }
    }
    @DeleteMapping("api/book/{id}")
    public  ResponseEntity<?> deleteBook(@PathVariable Long id)
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
    @PostMapping("api/book")
    public ResponseEntity<?> createBook(@RequestBody Book book) throws BookValidationException {
        ResponseEntity<?> responseEntity = bookService.saveBook(book);
        return responseEntity;
    }
    @PutMapping("api/book/{id}")
    public ResponseEntity<?> updateBook(@RequestBody Book book, @PathVariable Long id)
    {
       ResponseEntity<?> responseEntity= bookService.updateBook(book,id);
       return responseEntity;
    }
}
