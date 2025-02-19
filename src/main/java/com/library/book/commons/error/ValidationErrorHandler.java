package com.library.book.commons.error;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.library.book.commons.exeception.BookNotFoundException;

@RestControllerAdvice
public class ValidationErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ValidationErrorHandler.class);

    @Autowired
    private MessageSource messageSource;
    
    @ExceptionHandler({NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoHandlerFoundException(NoResourceFoundException ex) {
        logger.error("No handler found for the requested URL: {}", ex.getResourcePath(), ex);
        return new ResponseEntity<>("The requested URL was not found on the server.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        logger.error("No handler found for the requested URL: {}", ex.getRequestURL(), ex);
        return new ResponseEntity<>("The requested URL was not found on the server.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException ex) {
        logger.warn("Book not found: {}", ex.getMessage());
        return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
    }
    
    
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception, WebRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> 
            logger.warn("Validation failed for field '{}': {} (rejected value: {})", 
                        fieldError.getField(), 
                        fieldError.getDefaultMessage(), 
                        fieldError.getRejectedValue())
        );
        return new CreateErrorResponseAPI(fieldErrors, messageSource).create();
    }

    private static class CreateErrorResponseAPI {

        private List<ErrorResponse> errors = new ArrayList<>();
        private List<FieldError> fieldErrors;
        private MessageSource messageSource;

        public CreateErrorResponseAPI(List<FieldError> fieldErrors, MessageSource messageSource) {
            this.fieldErrors = fieldErrors;
            this.messageSource = messageSource;
        }

        public List<ErrorResponse> create() {
            fieldErrors.forEach(fieldError -> {
                String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                errors.add(new ErrorResponse(message, fieldError.getField(), fieldError.getRejectedValue()));
            });
            return errors;
        }
    }
}
