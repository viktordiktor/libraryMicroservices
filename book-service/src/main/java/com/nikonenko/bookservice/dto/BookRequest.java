package com.nikonenko.bookservice.dto;

import com.nikonenko.bookservice.models.BookGenre;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String isbn;

    private String title;

    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    private String description;

    private String author;
}
