package com.nikonenko.libraryservice.repositories;

import com.nikonenko.libraryservice.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("FROM Note n WHERE (n.borrowedDate IS NULL AND n.returnDate IS NULL) OR CURRENT_DATE > n.returnDate")
    List<Note> findFreeNotes();

    Optional<Note> findByBookId(Long bookId);
}
