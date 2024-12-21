package com.example.bookservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookWithoutIdDTO {
    @Pattern(
            regexp = "\\d-\\d{3}-\\d{5}-\\d{4}",
            message = "ISBN must be in the format D-DDD-DDDDD-DDDD"
    )
    private String isbn;
    @NotBlank(message = "The title field must not empty")
    private String title;
    @NotBlank(message = "The genre field must not empty")
    private String genre;
    @NotBlank(message = "The description field must not empty")
    private String description;
    @NotBlank(message = "The author field must not empty")
    private String author;
}
