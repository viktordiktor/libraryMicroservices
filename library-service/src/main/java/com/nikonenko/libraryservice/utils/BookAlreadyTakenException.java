package com.nikonenko.libraryservice.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookAlreadyTakenException extends RuntimeException {
    public BookAlreadyTakenException() {
        super("The book has already been taken.");
    }
}