package com.example.bookservice.service;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.dto.BookWithoutIdDTO;
import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.BookValidationException;
import com.example.bookservice.mapper.BookMapper;
import com.example.bookservice.mapper.BookWithoutIdMapper;
import com.example.bookservice.repository.BookRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private BookService bookService;

    private final BookMapper bookMapper = BookMapper.INSTANCE;
    private final BookWithoutIdMapper bookWithoutIdMapper = BookWithoutIdMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllBooks_ShouldReturnListOfBooks() {
        Book book = new Book(1L, "1-123-12345-1234", "Title", "Genre", "Description", "Author");
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));

        List<BookDTO> books = bookService.findAllBooks();

        assertEquals(1, books.size());
        assertEquals("Title", books.get(0).getTitle());
    }

    @Test
    void findBookById_ShouldReturnBook_WhenBookExists() {
        Book book = new Book(1L, "1-123-12345-1234", "Title", "Genre", "Description", "Author");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookWithoutIdDTO result = bookService.findBookById(1L);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    void findBookById_ShouldThrowException_WhenBookDoesNotExist() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.findBookById(1L));
        assertEquals("Book with Id: 1 not found", exception.getMessage());
    }

    @Test
    void findBookByIsbn_ShouldReturnBook_WhenBookExists() {
        Book book = new Book(1L, "1-123-12345-1234", "Title", "Genre", "Description", "Author");
        when(bookRepository.findByIsbn("1-123-12345-1234")).thenReturn(Optional.of(book));

        BookDTO result = bookService.findBookByIsbn("1-123-12345-1234");

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    void findBookByIsbn_ShouldThrowException_WhenBookDoesNotExist() {
        when(bookRepository.findByIsbn("1-123-12345-1234")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.findBookByIsbn("1-123-12345-1234"));
        assertEquals("Book with Isbn: 1-123-12345-1234 not found", exception.getMessage());
    }

    @Test
    void deleteById_ShouldThrowException_WhenBookDoesNotExist() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(1L, "token"));
        assertEquals("Book with ID 1 not found", exception.getMessage());
    }

    @Test
    void updateBook_ShouldUpdateBook_WhenBookExists() {
        Book book = new Book(1L, "1-123-12345-1234", "Old Title", "Old Genre", "Old Description", "Old Author");
        BookDTO updatedBookDTO = new BookDTO(1L, "1-123-12345-1234", "New Title", "New Genre", "New Description", "New Author");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.updateBook(1L, updatedBookDTO);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookDoesNotExist() {
        BookDTO updatedBookDTO = new BookDTO(1L, "1-123-12345-1234", "New Title", "New Genre", "New Description", "New Author");
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(1L, updatedBookDTO));
        assertEquals("Book with ID 1 not found", exception.getMessage());
    }
}
