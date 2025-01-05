package com.example.librarybookservice.repository;

import com.example.librarybookservice.entity.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook,Long> {
    Optional<LibraryBook> findByIdBook(Long idBook);
    @Query(value = "SELECT * FROM librarybook " +
            "WHERE (return_by IS NULL AND borrowed_at IS NULL) " +
            "OR (return_by < NOW() AND borrowed_at IS NULL) " +
            "OR (return_by < NOW() AND borrowed_at IS NOT NULL)", nativeQuery = true)
    List<LibraryBook> findAvailableBooks();
    void deleteByIdBook(Long idBook);
    boolean existsByIdBook(Long id);
}

