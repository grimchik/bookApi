package com.example.bookservice;

import com.example.bookservice.Dto.LibraryBookDTO;
import com.example.bookservice.Dto.LibraryBookWithoutIdDTO;
import com.example.bookservice.Entity.LibraryBook;
import com.example.bookservice.Mapper.LibraryBookMapper;
import com.example.bookservice.Mapper.LibraryBookWithoutIdMapper;
import com.example.bookservice.Repository.LibraryBookRepository;
import com.example.bookservice.Service.LibraryBookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class BookserviceApplicationTests {
	@Mock
	private LibraryBookRepository libraryBookRepository;

	@Mock
	private LibraryBookMapper libraryBookMapper;

	@Mock
	private LibraryBookWithoutIdMapper libraryBookWithoutIdMapper;

	@InjectMocks
	private LibraryBookService libraryBookService;

	private LibraryBook libraryBook;
	private LibraryBookDTO libraryBookDTO;
	private LibraryBookWithoutIdDTO libraryBookWithoutIdDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		libraryBook = new LibraryBook();
		libraryBook.setIdBook(1L);
		libraryBook.setBorrowedAt(null);
		libraryBook.setReturnBy(null);

		libraryBookDTO = new LibraryBookDTO();
		libraryBookDTO.setIdBook(1L);

		libraryBookWithoutIdDTO = new LibraryBookWithoutIdDTO();
		libraryBookWithoutIdDTO.setBorrowedAt(LocalDateTime.now().plusDays(1));
		libraryBookWithoutIdDTO.setReturnBy(LocalDateTime.now().plusDays(2));
	}

	@Test
	void saveLibraryBook_WhenIdIsNotNull() {
		when(libraryBookRepository.save(Mockito.any(LibraryBook.class))).thenReturn(libraryBook);
		when(libraryBookMapper.toDTO(Mockito.any(LibraryBook.class))).thenReturn(libraryBookDTO);

		LibraryBookDTO savedBookDTO = libraryBookService.saveLibraryBook(1L);

		assertNotNull(savedBookDTO);
		assertEquals(1L, savedBookDTO.getIdBook());
		verify(libraryBookRepository, times(1)).save(Mockito.any(LibraryBook.class));
	}

	@Test
	void saveLibraryBook_WhenIdIsNull() {
		assertThrows(IllegalArgumentException.class, () -> libraryBookService.saveLibraryBook(null));
	}

	@Test
	void getAvailableBooks_WhenThereAreAvailableBooks() {
		when(libraryBookRepository.findByReturnByBeforeOrReturnByIsNull(Mockito.any(LocalDateTime.class)))
				.thenReturn(List.of(libraryBook));
		when(libraryBookMapper.toDTO(Mockito.any(LibraryBook.class))).thenReturn(libraryBookDTO);

		List<LibraryBookDTO> availableBooks = libraryBookService.getAvailableBooks();

		assertNotNull(availableBooks);
		assertEquals(1, availableBooks.size());
	}

	@Test
	void updateBook_WhenValidDataIsProvided() {
		when(libraryBookRepository.findByIdBook(1L)).thenReturn(Optional.of(libraryBook));
		when(libraryBookRepository.save(Mockito.any(LibraryBook.class))).thenReturn(libraryBook);
		when(libraryBookWithoutIdMapper.toDTO(Mockito.any(LibraryBook.class))).thenReturn(libraryBookWithoutIdDTO);

		LibraryBookWithoutIdDTO updatedBookDTO = libraryBookService.updateBook(1L, libraryBookWithoutIdDTO);

		assertNotNull(updatedBookDTO);
		assertEquals(libraryBookWithoutIdDTO.getBorrowedAt(), updatedBookDTO.getBorrowedAt());
		verify(libraryBookRepository, times(1)).save(Mockito.any(LibraryBook.class));
	}

	@Test
	void updateBook_WhenBookIsNotFound() {
		when(libraryBookRepository.findByIdBook(1L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> libraryBookService.updateBook(1L, libraryBookWithoutIdDTO));
	}

}
