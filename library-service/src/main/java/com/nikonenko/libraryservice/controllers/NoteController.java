package com.nikonenko.libraryservice.controllers;

import com.nikonenko.libraryservice.dto.NoteResponse;
import com.nikonenko.libraryservice.services.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestController
@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/api/note")
@Tag(name="Note Controller", description="Responsible for Library notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/free")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get free notes",
            description = "Returns all notes that are free"
    )
    public List<NoteResponse> getFreeNotes(){
        return noteService.getFreeNotes();
    }

    @PostMapping("/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add note",
            description = "Adds note about new book"
    )
    public NoteResponse addNote(@PathVariable Long bookId){
        return noteService.addBlankNote(bookId);
    }

    @PatchMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Take book",
            description = "Edits borrowed date and return date"
    )
    public NoteResponse takeBook(@PathVariable Long bookId){
        return noteService.takeBook(bookId);
    }
}
