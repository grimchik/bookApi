package com.example.bookservice.Service;

import com.example.bookservice.Entity.Book;
import com.example.bookservice.Exception.BookValidationException;
import com.example.bookservice.Repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    private static final String ISBN_PATTERN = "^[0-9]-[0-9]{3}-[0-9]{5}-[0-9]{4}$";
    private static final Pattern isbnPattern = Pattern.compile(ISBN_PATTERN);
    private static final String AUTHOR_PATTERN = "^[^0-9]*$";
    private static final Pattern authorPattern = Pattern.compile(AUTHOR_PATTERN);
    private void validateBook(Book book) throws BookValidationException {
        if (book.getIsbn() == null || !isValidIsbn(book.getIsbn())) {
            throw new BookValidationException("ISBN must be in the format N-NNN-NNNNN-NNNN");
        }
        if (book.getAuthor() == null || !isValidAuthor(book.getAuthor())) {
            throw new BookValidationException("The author field must not contain numbers");
        }
    }
    private boolean isValidIsbn(String isbn) {
        return isbnPattern.matcher(isbn).matches();
    }

    private boolean isValidAuthor(String author) {
        return authorPattern.matcher(author).matches();
    }
    public List<Book> findAllBooks()
    {
        return bookRepository.findAll();
    }
    public Optional<Book> findBookById(Long id)
    {
        return bookRepository.findById(id);
    }
    public Optional<Book> findBookByIsbn(String isbn)
    {
        return bookRepository.findByIsbn(isbn);
    }
    public void deleteById(Long id) throws EntityNotFoundException {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
    }
    public void deleteByBook(Book book)
    {
        bookRepository.delete(book);
    }
    @Transactional
    public ResponseEntity<?> saveBook(Book book) throws BookValidationException{
        if (bookRepository.findByIsbn(book.getIsbn()).isEmpty()) {
            try{
                validateBook(book);
                Book savedBook = bookRepository.save(book);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
            }
            catch (BookValidationException e)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @Transactional
    public ResponseEntity<?> updateBook(Book book,Long id)
    {
        try{
            validateBook(book);
            if (bookRepository.findById(bookRepository.findByIsbn(book.getIsbn()).get().getId()).isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            else
            {
                bookRepository.save(book,id);
                return ResponseEntity.status(HttpStatus.OK).body(excitingBook);
            }
        }
        catch (BookValidationException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
