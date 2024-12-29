package com.example.librarybookservice.controller;

import com.example.librarybookservice.dto.LibraryBookDTO;
import com.example.librarybookservice.dto.LibraryBookWithoutIdDTO;
import com.example.librarybookservice.service.LibraryBookService;
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
    public ResponseEntity<?> getAvailableBooks() {
        return ResponseEntity.ok().body(librarybookService.getAvailableBooks());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLibraryBook(@RequestBody LibraryBookWithoutIdDTO bookDTO, @PathVariable Long id)  {
        return ResponseEntity.ok().body(librarybookService.updateBook(id, bookDTO));
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<?> createLibraryBook(@PathVariable Long id) {
        return ResponseEntity.ok(librarybookService.saveLibraryBook(id));
    }
}

