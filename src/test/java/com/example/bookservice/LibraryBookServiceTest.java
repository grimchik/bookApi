package com.example.bookservice;
import com.example.bookservice.dto.LibraryBookDTO;
import com.example.bookservice.dto.LibraryBookWithoutIdDTO;
import com.example.bookservice.entity.LibraryBook;
import com.example.bookservice.repository.LibraryBookRepository;
import com.example.bookservice.service.LibraryBookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class LibraryBookServiceTest {

	@Mock
	private LibraryBookRepository libraryBookRepository;

	@InjectMocks
	private LibraryBookService libraryBookService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSaveLibraryBook_Success() {
		Long bookId = 1L;
		LibraryBook libraryBook = new LibraryBook();
		libraryBook.setIdBook(bookId);

		when(libraryBookRepository.save(any(LibraryBook.class))).thenReturn(libraryBook);

		LibraryBookDTO savedLibraryBook = libraryBookService.saveLibraryBook(bookId);

		assertNotNull(savedLibraryBook);
		assertEquals(bookId, savedLibraryBook.getIdBook());
		verify(libraryBookRepository).save(any(LibraryBook.class));
	}

	@Test
	void testGetAvailableBooks() {
		LibraryBook availableBook = new LibraryBook();
		availableBook.setReturnBy(LocalDateTime.now().plusDays(1));

		when(libraryBookRepository.findByReturnByBeforeOrReturnByIsNull(any(LocalDateTime.class)))
				.thenReturn(List.of(availableBook));

		List<LibraryBookDTO> availableBooks = libraryBookService.getAvailableBooks();

		assertNotNull(availableBooks);
		assertEquals(1, availableBooks.size());
	}


	@Test
	void testUpdateBook_NotFound() {
		Long bookId = 999L;
		LibraryBookWithoutIdDTO updatedBookDTO = new LibraryBookWithoutIdDTO();

		when(libraryBookRepository.findByIdBook(bookId)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> libraryBookService.updateBook(bookId, updatedBookDTO));
	}
}
