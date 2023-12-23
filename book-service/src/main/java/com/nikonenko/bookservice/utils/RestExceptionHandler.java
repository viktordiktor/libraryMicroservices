package com.nikonenko.bookservice.utils;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler {
    private static final String INVALID_DATA_FORMAT_MESSAGE = "Invalid data format. Please provide valid data.";
    private static final String DATA_NOT_FOUND_MESSAGE = "Data not found!";
    private static final String CONSTRAINTS_ERROR_MESSAGE = "Check constraints!";
    private static final String WEBCLIENT_ERROR_MESSAGE = "Error during request to other service!";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_DATA_FORMAT_MESSAGE);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DATA_NOT_FOUND_MESSAGE);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CONSTRAINTS_ERROR_MESSAGE);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex){
        return ResponseEntity.status(ex.getStatusCode()).body(WEBCLIENT_ERROR_MESSAGE);
    }
}