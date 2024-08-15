package com.shadoww.exception;


import com.shadoww.api.exception.NotFoundException;
import com.shadoww.api.exception.NullEntityReferenceException;
import com.shadoww.api.exception.ValueAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        System.out.println("illegal");
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(NotFoundException e, WebRequest request) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NullEntityReferenceException.class)
    public ResponseEntity<?> handleEntityNotFoundException(NullEntityReferenceException e, WebRequest request) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ValueAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleValueAlreadyExistsException(ValueAlreadyExistsException e, WebRequest request) {
        return buildErrorResponse(e, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleInternalServerError(Exception e, WebRequest request) {

        System.out.println(e.getMessage());

        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    protected ResponseEntity<ExceptionResponse> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, status);
    }

    protected ResponseEntity<ExceptionResponse> buildErrorResponse(Exception ex, String message, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, status);
    }

}
