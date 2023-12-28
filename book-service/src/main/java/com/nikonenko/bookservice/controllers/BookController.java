package com.nikonenko.bookservice.controllers;

import com.nikonenko.bookservice.dto.BookRequest;
import com.nikonenko.bookservice.dto.BookResponse;
import com.nikonenko.bookservice.dto.NoteResponse;
import com.nikonenko.bookservice.service.BookService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.awt.print.Book;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
@RestControllerAdvice
@Tag(name="Book Controller", description="Responsible for Books data")
public class BookController{
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "library")
    @Retry(name = "library")
    @Operation(
            summary = "Create book",
            description = "Creates book and returns created book"
    )
    public CompletableFuture<BookResponse> createBook(@RequestBody BookRequest bookRequest){
        return CompletableFuture.supplyAsync(() -> bookService.createBook(bookRequest));
    }

    public CompletableFuture<ResponseEntity<String>> fallbackMethod(BookRequest bookRequest,
                                                                    RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(() ->
                ResponseEntity.status(500).body(runtimeException.getMessage()));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all books",
            description = "Returns all books"
    )
    public List<BookResponse> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get book by ID",
            description = "Returns books by ID"
    )
    public BookResponse getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @GetMapping("/isbn/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get book by ISBN",
            description = "Returns book by ISBN"
    )
    public BookResponse getBookByIsbn(@PathVariable String isbn){
        return bookService.getBookByIsbn(isbn);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Edit book",
            description = "Edits book by ID"
    )
    public BookResponse editBook(@PathVariable Long id, @RequestBody BookRequest bookRequest){
        return bookService.editBook(id, bookRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete book",
            description = "Deletes book by ID"
    )
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Take Book",
            description = "Returns borrowed and return date of book by ID"
    )
    public NoteResponse takeBook(@PathVariable Long id){
        return bookService.takeBook(id);
    }
}
