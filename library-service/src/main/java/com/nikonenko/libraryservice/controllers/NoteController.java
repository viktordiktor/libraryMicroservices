package com.nikonenko.libraryservice.controllers;

import com.nikonenko.libraryservice.dto.NoteResponse;
import com.nikonenko.libraryservice.services.NoteService;
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
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/free")
    @ResponseStatus(HttpStatus.OK)
    public List<NoteResponse> getFreeNotes(){
        return noteService.getFreeNotes();
    }

    @PostMapping("/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse addNote(@PathVariable Long bookId){
        return noteService.addBlankNote(bookId);
    }

    @PatchMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public NoteResponse takeBook(@PathVariable Long bookId){
        return noteService.takeBook(bookId);
    }
}
