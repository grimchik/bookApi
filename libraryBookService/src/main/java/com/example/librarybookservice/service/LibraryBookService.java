package com.example.librarybookservice.service;

import com.example.librarybookservice.dto.LibraryBookDTO;
import com.example.librarybookservice.dto.LibraryBookWithoutIdDTO;
import com.example.librarybookservice.entity.LibraryBook;
import com.example.librarybookservice.mapper.LibraryBookMapper;
import com.example.librarybookservice.mapper.LibraryBookWithoutIdMapper;
import com.example.librarybookservice.repository.LibraryBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryBookService {

    @Autowired
    private LibraryBookRepository libraryBookRepository;

    private final LibraryBookMapper libraryBookMapper = LibraryBookMapper.INSTANCE;
    private final LibraryBookWithoutIdMapper libraryBookWithoutIdMapper = LibraryBookWithoutIdMapper.INSTANCE;

    @Transactional
    public LibraryBookDTO saveLibraryBook(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }

        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setIdBook(id);
        libraryBook.setBorrowedAt(null);
        libraryBook.setReturnBy(null);

        libraryBookRepository.save(libraryBook);

        return libraryBookMapper.toDTO(libraryBook);
    }

    public List<LibraryBookDTO> getAvailableBooks() {
        List<LibraryBook> availableBooks = libraryBookRepository.findAvailableBooks();
        return availableBooks.stream()
                .map(LibraryBookMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LibraryBookWithoutIdDTO updateBook(Long id, LibraryBookWithoutIdDTO updatedBookDTO) throws EntityNotFoundException, IOException {
        return libraryBookRepository.findByIdBook(id)
                .map(book -> {
                    if (updatedBookDTO.getBorrowedAt() != null && !updatedBookDTO.getBorrowedAt().toString().isBlank()) {
                        if (updatedBookDTO.getBorrowedAt().isAfter(LocalDateTime.now())) {
                            throw new IllegalArgumentException("Borrowed date cannot be in the future");
                        }
                        book.setBorrowedAt(updatedBookDTO.getBorrowedAt());
                    }

                    if (updatedBookDTO.getReturnBy() != null && !updatedBookDTO.getReturnBy().toString().isBlank()) {
                        if (updatedBookDTO.getReturnBy().isBefore(LocalDateTime.now())) {
                            throw new IllegalArgumentException("Return date must be in the future");
                        }
                        if (book.getBorrowedAt() != null && updatedBookDTO.getReturnBy().isBefore(book.getBorrowedAt())) {
                            throw new IllegalArgumentException("Return date cannot be before borrowed date");
                        }
                        book.setReturnBy(updatedBookDTO.getReturnBy());
                    }

                    libraryBookRepository.save(book);

                    return libraryBookWithoutIdMapper.toDTO(book);
                })
                .orElseThrow(() -> new EntityNotFoundException("Library Book not found!"));
    }
}
