package com.booking.KBookin.exception;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.booking.KBookin.dto.ErrorResponse;

import jakarta.persistence.EntityExistsException;

@RestControllerAdvice
public class GlobalException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            EntityExistsException.class, EntityNotFoundException.class,
            EmailAlreadyExistsException.class,VerificationException.class,BusinessProcessException.class
    })
    public ResponseEntity<ErrorResponse<Object>> handleBadRequestException(RuntimeException exception) {
        ErrorResponse<Object> response = new ErrorResponse<>();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setError(exception.getMessage());
        response.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Object>> handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse<Object> response = new ErrorResponse<>();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setError("Bad request");
        response.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ErrorResponse<Object>> handleAuthenticationException(Exception ex) {
        ErrorResponse<Object> response = new ErrorResponse<>();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setError(ex.getMessage());
        response.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse<Object>> handleUnauthorizedAccessException(AccessDeniedException ex) {
        ErrorResponse<Object> response = new ErrorResponse<>();
        response.setStatusCode(HttpStatus.FORBIDDEN.value());
        response.setError(ex.getMessage());
        response.setMessage("Unauthorized access. You do not have permission to access this resource.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
