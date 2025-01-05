package com.example.bookservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    @Pattern(
            regexp = "^[0-9]-[0-9]{3}-[0-9]{5}-[0-9]{4}$",
            message = "ISBN must be in the format D-DDD-DDDDD-DDDD"
    )
    private String isbn;
    @NotBlank(message = "The title field must not empty")
    private String title;
    @NotBlank(message = "The genre field must not empty")
    private String genre;
    private String description;
    @Pattern(
            regexp = "^[^0-9]*$",
            message = "The author field must not contain numbers"
    )
    private String author;
}