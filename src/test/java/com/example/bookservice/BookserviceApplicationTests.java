package com.example.bookservice;

import com.example.bookservice.Dto.BookDTO;
import com.example.bookservice.Entity.Book;
import com.example.bookservice.Repository.BookRepository;
import com.example.bookservice.Repository.LibraryBookRepository;
import com.example.bookservice.Service.BookService;
import com.example.bookservice.Service.ExternalServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.bookservice.Dto.BookDTO;
import com.example.bookservice.Entity.Book;
import com.example.bookservice.Exception.BookValidationException;
import com.example.bookservice.Mapper.BookMapper;
import com.example.bookservice.Repository.BookRepository;
import com.example.bookservice.Repository.LibraryBookRepository;
import com.example.bookservice.Exception.BookValidationException;
import com.example.bookservice.Entity.LibraryBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookserviceApplicationTests {
	@Mock
	private BookRepository bookRepository;

	@Mock
	private LibraryBookRepository libraryBookRepository;

	@Mock
	private ExternalServiceClient externalServiceClient;

	@InjectMocks
	private BookService bookService;

	private BookMapper bookMapper = BookMapper.INSTANCE;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	@Test
	void saveBook_shouldSaveBookAndReturnBookDTO() throws BookValidationException {
		BookDTO bookDTO = new BookDTO();
		bookDTO.setIsbn("1-111-11111-0001");
		bookDTO.setTitle("title1");
		bookDTO.setGenre("genre1");
		bookDTO.setDescription("des1");
		bookDTO.setAuthor("one");
		Book book = bookMapper.toEntity(bookDTO);
		when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(Optional.empty());
		when(bookRepository.save(any(Book.class))).thenReturn(book);
		BookDTO savedBookDTO = bookService.saveBook(bookDTO, "someToken");
		assertNotNull(savedBookDTO);
		assertEquals("1-111-11111-0001", savedBookDTO.getIsbn());
		verify(externalServiceClient, times(1)).addBookToExternalService(anyLong(), eq("someToken"));
	}

}
