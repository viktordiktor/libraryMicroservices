package com.nikonenko.authservice.utils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.CredentialExpiredException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler {
    private static final String INVALID_DATA_FORMAT_MESSAGE = "Invalid data format. Please provide valid data.";
    private static final String DATA_NOT_FOUND_MESSAGE = "Data not found!";
    private static final String CONSTRAINTS_ERROR_MESSAGE = "Check constraints!";
    private static final String EXPIRED_ERROR_MESSAGE = "Token expired!";
    private static final String ALREADY_EXISTS_ERROR_MESSAGE = "User already exists!";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_DATA_FORMAT_MESSAGE);
    }

    @ExceptionHandler({CredentialExpiredException.class, ExpiredJwtException.class})
    public ResponseEntity<String> handleCredentialExpiredException(CredentialExpiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(EXPIRED_ERROR_MESSAGE);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DATA_NOT_FOUND_MESSAGE);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleEntityExistsException(EntityExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ALREADY_EXISTS_ERROR_MESSAGE);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CONSTRAINTS_ERROR_MESSAGE);
    }


}