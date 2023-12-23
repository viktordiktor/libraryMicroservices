package com.nikonenko.bookservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {
    private Long id;

    private Long bookId;

    private LocalDateTime borrowedDate;

    private LocalDateTime returnDate;
}
