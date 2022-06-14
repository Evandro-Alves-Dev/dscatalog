package com.devsuperior.dscatalog.exceptions.handle;

import com.devsuperior.dscatalog.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class StandardHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest http) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(StandardError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource not found")
                .message(e.getMessage())
                .path(http.getRequestURI())
                .build());
    }
}
