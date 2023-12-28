package com.nikonenko.bookservice.service;

import com.nikonenko.bookservice.dto.BookRequest;
import com.nikonenko.bookservice.dto.BookResponse;
import com.nikonenko.bookservice.dto.NoteResponse;
import com.nikonenko.bookservice.models.Book;
import com.nikonenko.bookservice.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final WebClient.Builder webClientBuilder;

    public BookResponse createBook(BookRequest bookRequest){
        Book book = modelMapper.map(bookRequest, Book.class);
        bookRepository.save(book);

        webClientBuilder.build()
                .post()
                .uri("http://library-service/api/note/" + book.getId())
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(throwable -> {
                    deleteBook(book.getId());
                    return Mono.error(throwable);
                })
                .block();


        log.info("Book {} saved", book.getId());
        return modelMapper.map(book, BookResponse.class);
    }

    public List<BookResponse> getAllBooks() {
        TypeToken<List<BookResponse>> responseTypeToken = new TypeToken<>() {};
        return modelMapper.map(bookRepository.findAll(), responseTypeToken.getType());
    }

    public BookResponse getBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isEmpty())
            throw new EntityNotFoundException();
        return modelMapper.map(optionalBook.get(), BookResponse.class);
    }

    public BookResponse getBookByIsbn(String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if(optionalBook.isEmpty())
            throw new EntityNotFoundException();
        return modelMapper.map(optionalBook.get(), BookResponse.class);
    }

    public BookResponse editBook(Long id, BookRequest bookRequest) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isEmpty())
            throw new EntityNotFoundException();
        Book book = optionalBook.get();
        modelMapper.map(bookRequest, book);
        bookRepository.save(book);
        log.info("Book {} edited", book.getId());
        return modelMapper.map(book, BookResponse.class);
    }

    public void deleteBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isEmpty())
            throw new EntityNotFoundException();
        bookRepository.delete(optionalBook.get());
        log.info("Book {} deleted", optionalBook.get().getId());
    }

    public NoteResponse takeBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isEmpty())
            throw new EntityNotFoundException();
        return webClientBuilder.build()
                .patch()
                .uri("http://library-service/api/note/" + optionalBook.get().getId())
                .retrieve()
                .bodyToMono(NoteResponse.class)
                .onErrorResume(WebClientResponseException.class, Mono::error)
                .block();
    }
}
