package com.example.bookservice.Service;

import com.example.bookservice.Dto.BookDTO;
import com.example.bookservice.Entity.Book;
import com.example.bookservice.Exception.BookValidationException;
import com.example.bookservice.Mapper.BookMapper;
import com.example.bookservice.Repository.BookRepository;
import com.example.bookservice.Repository.LibraryBookRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.bookservice.Entity.LibraryBook;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

    @Service
    public class BookService {
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
        private static final String ISBN_PATTERN = "^[0-9]-[0-9]{3}-[0-9]{5}-[0-9]{4}$";
        private static final Pattern isbnPattern = Pattern.compile(ISBN_PATTERN);
        private static final String AUTHOR_PATTERN = "^[^0-9]*$";
        private static final Pattern authorPattern = Pattern.compile(AUTHOR_PATTERN);

        private void validateBook(Book book) throws BookValidationException {
            if (book.getIsbn() == null || !isValidIsbn(book.getIsbn()) ) {
                throw new BookValidationException("ISBN must be in the format D-DDD-DDDDD-DDDD");
            }
            if (  book.getAuthor() == null || !isValidAuthor(book.getAuthor())) {
                throw new BookValidationException("The author field must not contain numbers");
            }
            if (book.getAuthor() == "" )
            {
                throw new BookValidationException("The author field must not empty");
            }
            if (book.getTitle() == "" )
            {
                throw new BookValidationException("The title field must not empty");
            }
            if (book.getTitle() == null)
            {
                throw new BookValidationException("The Title field must not contain empty");
            }
            if (book.getGenre() == null)
            {
                throw new BookValidationException("The Genre field must not contain empty");
            }
            if (book.getDescription() == null)
            {
                throw new BookValidationException("The Description field must not empty");
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
            LibraryBook libraryBook = libraryBookRepository.findByIdBook(id)
                    .orElseThrow(() -> new EntityNotFoundException("Library book with ID Book " + id + " not found"));
            libraryBookRepository.deleteById(libraryBook.getId());
        }

        @Transactional
        public BookDTO saveBook(BookDTO bookDTO,String token) throws BookValidationException, EntityExistsException {

            if (bookRepository.findByIsbn(bookDTO.getIsbn()).isPresent()) {
                throw new EntityExistsException("Book with the same ISBN already exists");
            }
            validateBook(bookMapper.toEntity(bookDTO));
            Book book = bookMapper.toEntity(bookDTO);
            Book savedBook = bookRepository.save(book);
            externalServiceClient.addBookToExternalService(savedBook.getId(),token)
                    .doOnTerminate(() -> System.out.println("Book ID " + savedBook.getId() + " added to external service"))
                    .subscribe();
            return bookMapper.toDTO(savedBook);
        }

        @Transactional
        public BookDTO updateBook(Long id, BookDTO updatedBookDTO) throws BookValidationException, EntityNotFoundException {

            Book existingBook = bookRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " not found"));
            if (updatedBookDTO.getIsbn() != null && !updatedBookDTO.getIsbn().trim().isEmpty()) {
                if (!isValidIsbn(updatedBookDTO.getIsbn())) {
                    throw new BookValidationException("ISBN must be in the format D-DDD-DDDDD-DDDD");
                }
                existingBook.setIsbn(updatedBookDTO.getIsbn());
            }
            if (updatedBookDTO.getTitle() != null && !updatedBookDTO.getTitle().trim().isEmpty()) {
                existingBook.setTitle(updatedBookDTO.getTitle());
            }
            if (updatedBookDTO.getGenre() != null && !updatedBookDTO.getGenre().trim().isEmpty()) {
                existingBook.setGenre(updatedBookDTO.getGenre());
            }
            if (updatedBookDTO.getDescription() != null && !updatedBookDTO.getDescription().trim().isEmpty()) {
                existingBook.setDescription(updatedBookDTO.getDescription());
            }
            if (updatedBookDTO.getAuthor() != null && !updatedBookDTO.getAuthor().trim().isEmpty()) {
                if (!isValidAuthor(updatedBookDTO.getAuthor())) {
                    throw new BookValidationException("The author field must not contain numbers");
                }
                existingBook.setAuthor(updatedBookDTO.getAuthor());
            }
            Book updatedBook = bookRepository.save(existingBook);
            return bookMapper.toDTO(updatedBook);
        }


    }
