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
        try {
            return ResponseEntity.ok().body(librarybookService.getAvailableBooks());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLibraryBook(@RequestBody LibraryBookWithoutIdDTO bookDTO, @PathVariable Long id) {
        try {
            LibraryBookWithoutIdDTO updatedBook = librarybookService.updateBook(id, bookDTO);
            return ResponseEntity.ok().body(updatedBook);
        }
        catch (EntityNotFoundException exc) {
            return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
        catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please provide a valid date.");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/add/{id}")
    public ResponseEntity<?> createLibraryBook(@PathVariable Long id) {
        try {
            LibraryBookDTO savedBook = librarybookService.saveLibraryBook(id);
            return ResponseEntity.ok(savedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( e.getMessage());
        }
    }
}
