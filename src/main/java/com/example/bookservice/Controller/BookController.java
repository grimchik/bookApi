package com.example.bookservice.Controller;

import com.example.bookservice.Dto.BookDTO;
import com.example.bookservice.Entity.Book;
import com.example.bookservice.Exception.BookValidationException;
import com.example.bookservice.Repository.BookRepository;
import com.example.bookservice.Service.BookService;
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
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {
        try {
            return ResponseEntity.ok().body(bookService.findAllBooks());
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/book/id/{id}")
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
    @GetMapping("/book/isbn/{isbn}")
    public ResponseEntity<?> getBooksByIsbn(@PathVariable String isbn) {
        Optional<BookDTO> book=bookService.findBookByIsbn(isbn);
        if (book.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            return ResponseEntity.ok().body(book.get());
        }
    }
    @DeleteMapping("/book/{id}")
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
    @PostMapping("/book")
    public ResponseEntity<?> createBook(@RequestBody BookDTO book,@RequestHeader("Authorization") String authorizationHeader) throws BookValidationException {
        try
        {
            String token = authorizationHeader.replace("Bearer ", "");
            BookDTO bookDTO = bookService.saveBook(book,token);
            return new ResponseEntity<>(bookDTO,HttpStatus.CREATED);
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
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }
}
