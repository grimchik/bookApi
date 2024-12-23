package com.example.bookservice.controller;

import com.example.bookservice.dto.LibraryBookDTO;
import com.example.bookservice.dto.LibraryBookWithoutIdDTO;
import com.example.bookservice.service.LibraryBookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/library")
public class LibraryBookController {
    @Autowired
    private LibraryBookService librarybookService;

    @GetMapping("/available")
    public ResponseEntity<List<LibraryBookDTO>> getAvailableBooks() {
        return ResponseEntity.ok().body(librarybookService.getAvailableBooks());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLibraryBook(@RequestBody LibraryBookWithoutIdDTO bookDTO, @PathVariable Long id) throws IOException {
        LibraryBookWithoutIdDTO updatedBook = librarybookService.updateBook(id, bookDTO);
        return ResponseEntity.ok().body(updatedBook);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<?> createLibraryBook(@PathVariable Long id) {
        LibraryBookDTO savedBook = librarybookService.saveLibraryBook(id);
        return ResponseEntity.ok(savedBook);
    }
}
