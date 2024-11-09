package com.example.bookservice.Repository;

import com.example.bookservice.Entity.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook,Long> {
    Optional<LibraryBook> findByIdBook(Long idBook);
    List<LibraryBook> findByReturnByBeforeOrReturnByIsNull(LocalDateTime currentTime);
    void deleteByIdBook(Long idBook);
}
