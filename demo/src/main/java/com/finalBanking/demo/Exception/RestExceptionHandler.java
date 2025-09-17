package com.finalBanking.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class RestExceptionHandler {
    record ErrorPayload(LocalDateTime timestamp, int status, String error, String message) {}

    @ExceptionHandler({ AccountNotFoundException.class, InsufficientFundsException.class, LimitExceededException.class, IllegalArgumentException.class })
    public ResponseEntity<ErrorPayload> business(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorPayload(LocalDateTime.now(), 400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorPayload> validation(MethodArgumentNotValidException ex) {
        String msg = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorPayload(LocalDateTime.now(), 400, "Validation Error", msg));
    }
}
