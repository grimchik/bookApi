package com.example.librarybookservice;

import com.example.librarybookservice.dto.LibraryBookDTO;
import com.example.librarybookservice.dto.LibraryBookWithoutIdDTO;
import com.example.librarybookservice.entity.LibraryBook;
import com.example.librarybookservice.mapper.LibraryBookMapper;
import com.example.librarybookservice.mapper.LibraryBookWithoutIdMapper;
import com.example.librarybookservice.repository.LibraryBookRepository;
import com.example.librarybookservice.service.LibraryBookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class LibraryBookServiceApplicationTests {

    @Mock
    private LibraryBookRepository libraryBookRepository;

    @InjectMocks
    private LibraryBookService libraryBookService;

    private final LibraryBookMapper libraryBookMapper = LibraryBookMapper.INSTANCE;
    private final LibraryBookWithoutIdMapper libraryBookWithoutIdMapper = LibraryBookWithoutIdMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAvailableBooks_ShouldReturnListOfAvailableBooks() {
        LibraryBook libraryBook = new LibraryBook(null, 1L, null, null);
        when(libraryBookRepository.findAvailableBooks()).thenReturn(Collections.singletonList(libraryBook));

        List<LibraryBookDTO> result = libraryBookService.getAvailableBooks();

        assertEquals(1, result.size());
        assertNull(result.get(0).getBorrowedAt());
        assertNull(result.get(0).getReturnBy());
    }

    @Test
    void updateBook_ShouldUpdateBook_WhenBookExists() {
        LibraryBook existingBook = new LibraryBook(1L, 1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));
        LibraryBookWithoutIdDTO updatedBookDTO = new LibraryBookWithoutIdDTO(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(10));

        when(libraryBookRepository.findByIdBook(1L)).thenReturn(Optional.of(existingBook));
        when(libraryBookRepository.save(any(LibraryBook.class))).thenReturn(existingBook);

        LibraryBookWithoutIdDTO result = libraryBookService.updateBook(1L, updatedBookDTO);

        assertNotNull(result);
        assertEquals(updatedBookDTO.getBorrowedAt(), result.getBorrowedAt());
        assertEquals(updatedBookDTO.getReturnBy(), result.getReturnBy());
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookDoesNotExist() {
        LibraryBookWithoutIdDTO updatedBookDTO = new LibraryBookWithoutIdDTO(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(10));
        when(libraryBookRepository.findByIdBook(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> libraryBookService.updateBook(1L, updatedBookDTO));
        assertEquals("Library Book not found!", exception.getMessage());
    }

    @Test
    void updateBook_ShouldThrowException_WhenBorrowedDateIsInTheFuture() {
        LibraryBook existingBook = new LibraryBook(1L, 1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));
        LibraryBookWithoutIdDTO updatedBookDTO = new LibraryBookWithoutIdDTO(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(10));

        when(libraryBookRepository.findByIdBook(1L)).thenReturn(Optional.of(existingBook));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> libraryBookService.updateBook(1L, updatedBookDTO));
        assertEquals("Borrowed date cannot be in the future", exception.getMessage());
    }

    @Test
    void updateBook_ShouldThrowException_WhenReturnDateIsInThePast() {
        LibraryBook existingBook = new LibraryBook(1L, 1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));
        LibraryBookWithoutIdDTO updatedBookDTO = new LibraryBookWithoutIdDTO(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));

        when(libraryBookRepository.findByIdBook(1L)).thenReturn(Optional.of(existingBook));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> libraryBookService.updateBook(1L, updatedBookDTO));
        assertEquals("Return date must be in the future", exception.getMessage());
    }
}