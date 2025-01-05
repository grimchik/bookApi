package com.example.librarybookservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBookDTO {
    private Long idBook;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime borrowedAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime returnBy;
}
