package com.example.bookservice.Service;

import com.example.bookservice.Dto.LibraryBookDTO;
import com.example.bookservice.Dto.LibraryBookWithoutIdDTO;
import com.example.bookservice.Entity.LibraryBook;
import com.example.bookservice.Mapper.LibraryBookMapper;
import com.example.bookservice.Mapper.LibraryBookWithoutIdMapper;
import com.example.bookservice.Repository.LibraryBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryBookService {
    @Autowired
    private LibraryBookRepository libraryBookRepository;
    private final LibraryBookMapper mapper = LibraryBookMapper.INSTANCE;
    private final LibraryBookWithoutIdMapper mapper2 = LibraryBookWithoutIdMapper.INSTANCE;
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
        return mapper.toDTO(libraryBook);
    }

    public List<LibraryBookDTO> getAvailableBooks() {
        List<LibraryBook> availableBooks = libraryBookRepository.findByReturnByBeforeOrReturnByIsNull(LocalDateTime.now());
        return availableBooks.stream()
                .map(LibraryBookMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LibraryBookWithoutIdDTO updateBook(Long id, LibraryBookWithoutIdDTO updatedBookDTO) throws EntityNotFoundException {
        return libraryBookRepository.findByIdBook(id)
                .map(book -> {
                    if (updatedBookDTO.getBorrowedAt() != null) {
                        if (updatedBookDTO.getBorrowedAt().isBefore(LocalDateTime.now())) {
                            throw new IllegalArgumentException("Borrowed date cannot be in the past");
                        }
                        book.setBorrowedAt(updatedBookDTO.getBorrowedAt());
                    }
                    if (updatedBookDTO.getReturnBy() != null) {
                        if (updatedBookDTO.getReturnBy().isBefore(LocalDateTime.now())) {
                            throw new IllegalArgumentException("Return date must be in the future");
                        }
                        book.setReturnBy(updatedBookDTO.getReturnBy());
                    }
                    libraryBookRepository.save(book);
                    return mapper2.toDTO(book);
                })
                .orElseThrow(() -> new EntityNotFoundException("Library Book not found!"));
    }
}
