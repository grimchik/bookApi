package com.example.bookservice.service;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.dto.BookWithoutIdDTO;
import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.BookValidationException;
import com.example.bookservice.mapper.BookMapper;
import com.example.bookservice.mapper.BookWithoutIdMapper;
import com.example.bookservice.mapper.LibraryBookMapper;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.repository.LibraryBookRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.bookservice.entity.LibraryBook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;
    private final LibraryBookRepository libraryBookRepository;
    private final ExternalServiceClient externalServiceClient;

    @Autowired
    public BookService(BookRepository bookRepository, LibraryBookRepository libraryBookRepository, ExternalServiceClient externalServiceClient) {
        this.bookRepository = bookRepository;
        this.libraryBookRepository = libraryBookRepository;
        this.externalServiceClient = externalServiceClient;
    }

    private final BookMapper bookMapper = BookMapper.INSTANCE;
    private final BookWithoutIdMapper bookWithoutIdMapper = BookWithoutIdMapper.INSTANCE;

    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public BookWithoutIdDTO findBookById(Long id) throws EntityNotFoundException {
        return bookRepository.findById(id)
                .map(bookWithoutIdMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Book with Id: " + id + " not found"));
    }

    public BookDTO findBookByIsbn(String isbn) throws EntityNotFoundException {
        return bookRepository.findByIsbn(isbn)
                .map(bookMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Book with Isbn: " + isbn + " not found"));
    }

    @Transactional
    public void deleteById(Long id) throws EntityNotFoundException {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
        LibraryBook libraryBook = libraryBookRepository.findByIdBook(id)
                .orElseThrow(() -> new EntityNotFoundException("Library book with ID Book " + id + " not found"));
        libraryBookRepository.deleteById(libraryBook.getId());
    }

    @Transactional
    public BookWithoutIdDTO saveBook(BookWithoutIdDTO bookWithoutIdDDTO, String token) throws BookValidationException, EntityExistsException {
        if (bookRepository.findByIsbn(bookWithoutIdDDTO.getIsbn()).isPresent()) {
            throw new EntityExistsException("Book with the same ISBN already exists");
        }
        Book book = bookWithoutIdMapper.toEntity(bookWithoutIdDDTO);
        Book savedBook = bookRepository.save(book);
        externalServiceClient.addBookToExternalService(savedBook.getId(), token)
                .doOnTerminate(() -> logger.info("Book ID {} added to external service", savedBook.getId()))
                .subscribe();
        return bookWithoutIdMapper.toDTO(savedBook);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO updatedBookDTO) throws EntityNotFoundException {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " not found"));
        existingBook.setTitle(updatedBookDTO.getTitle());
        existingBook.setAuthor(updatedBookDTO.getAuthor());
        existingBook.setIsbn(updatedBookDTO.getIsbn());
        existingBook.setGenre(updatedBookDTO.getGenre());
        existingBook.setDescription(updatedBookDTO.getDescription());
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDTO(updatedBook);
    }
}
