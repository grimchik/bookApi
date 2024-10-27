package com.example.bookservice.Controller;

import com.example.bookservice.Entity.Book;
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
    public ResponseEntity<Book> createBook(@RequestBody Book book) {

    }

    /*@PatchMapping ("api/book/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id,@RequestBody Book book) {
        try {
            HttpStatus status = bookService.saveBook(book);
            return new ResponseEntity<>(book, status);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }*/
}
