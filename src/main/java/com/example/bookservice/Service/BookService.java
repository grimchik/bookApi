package com.example.bookservice.Service;

import com.example.bookservice.Dto.BookDTO;
import com.example.bookservice.Entity.Book;
import com.example.bookservice.Exception.BookValidationException;
import com.example.bookservice.Mapper.BookMapper;
import com.example.bookservice.Repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    private final BookMapper bookMapper = BookMapper.INSTANCE;
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

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<BookDTO> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).map(bookMapper::toDTO);
    }

    public void deleteById(Long id) throws EntityNotFoundException {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    @Transactional
    public BookDTO saveBook(BookDTO bookDTO) throws BookValidationException {
        validateBook(bookMapper.toEntity(bookDTO));
        if (bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
            throw new BookValidationException("Book with the same ISBN already exists");
        }
        Book book = bookMapper.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO updatedBookDTO) throws BookValidationException, EntityNotFoundException {
        validateBook(bookMapper.toEntity(updatedBookDTO));
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " not found"));

        existingBook.setIsbn(updatedBookDTO.getIsbn());
        existingBook.setTitle(updatedBookDTO.getTitle());
        existingBook.setGenre(updatedBookDTO.getGenre());
        existingBook.setDescription(updatedBookDTO.getDescription());
        existingBook.setAuthor(updatedBookDTO.getAuthor());

        return bookMapper.toDTO(bookRepository.save(existingBook));
    }
}
