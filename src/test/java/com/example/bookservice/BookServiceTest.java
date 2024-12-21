package com.example.bookservice;
import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.BookValidationException;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.repository.LibraryBookRepository;
import com.example.bookservice.service.BookService;
import com.example.bookservice.service.ExternalServiceClient;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LibraryBookRepository libraryBookRepository;

    @Mock
    private ExternalServiceClient externalServiceClient;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void saveBook_DuplicateBook_ThrowsException() {
        Book existingBook = new Book(1L, "123-456-78901-2345", "Existing Book", "Genre", "Author", "Description");
        BookDTO newBookDTO = new BookDTO("123-456-78901-2345", "New Book", "Genre", "Author", "Description");

        when(bookRepository.findByIsbn("123-456-78901-2345")).thenReturn(Optional.of(existingBook));

        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> {
            bookService.saveBook(newBookDTO, "test-token");
        });

        assertEquals("Book with the same ISBN already exists", exception.getMessage());

        verify(bookRepository, never()).save(any(Book.class));
        verify(externalServiceClient, never()).addBookToExternalService(anyLong(), anyString());
    }

    @Test
    void saveBook_BookWithInvalidIsbn_ThrowsValidationException() {
        BookDTO invalidBookDTO = new BookDTO("invalid-isbn", "Test Book", "Test Genre", "Test Author", "Test Description");

        assertThrows(BookValidationException.class, () -> {
            bookService.saveBook(invalidBookDTO, "test-token");
        });
    }
}
