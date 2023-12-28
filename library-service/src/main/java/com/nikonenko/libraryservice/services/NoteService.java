package com.nikonenko.libraryservice.services;

import com.nikonenko.libraryservice.dto.NoteResponse;
import com.nikonenko.libraryservice.models.Note;
import com.nikonenko.libraryservice.repositories.NoteRepository;
import com.nikonenko.libraryservice.utils.BookAlreadyTakenException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final ModelMapper modelMapper;

    public List<NoteResponse> getFreeNotes(){
        TypeToken<List<NoteResponse>> typeToken = new TypeToken<>() {};
        return modelMapper.map(noteRepository.findFreeNotes(), typeToken.getType());
    }

    public NoteResponse addBlankNote(Long bookId) {
        Optional<Note> noteOptional = noteRepository.findByBookId(bookId);
        noteOptional.ifPresent(note -> deleteNote(note.getId()));
        Note blankNote = Note.builder()
                .bookId(bookId).build();
        noteRepository.save(blankNote);
        log.info("Note {} saved", blankNote.getId());
        return modelMapper.map(blankNote, NoteResponse.class);
    }

    public NoteResponse takeBook(Long bookId) {
        Optional<Note> noteOptional = noteRepository.findByBookId(bookId);
        if (noteOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Note note = noteOptional.get();
        LocalDateTime currentDate = LocalDateTime.now();
        if (note.getReturnDate() != null && note.getReturnDate().isAfter(currentDate)) {
            throw new BookAlreadyTakenException();
        }

        note.setBorrowedDate(currentDate);
        note.setReturnDate(currentDate.plus(Duration.ofDays(7)));

        noteRepository.save(note);
        log.info("Note {} edited", note.getId());
        return modelMapper.map(note, NoteResponse.class);
    }

    public void deleteNote(Long id){
        Optional<Note> noteOptional = noteRepository.findById(id);
        if(noteOptional.isEmpty())
            throw new EntityNotFoundException();
        noteRepository.delete(noteOptional.get());
        log.info("Note {} deleted", id);
    }
}
