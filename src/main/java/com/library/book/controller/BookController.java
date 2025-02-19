package com.library.book.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.book.dto.CreateBookDTO;
import com.library.book.dto.FindBookDTO;
import com.library.book.dto.PageDTO;
import com.library.book.dto.UpdateBookDTO;
import com.library.book.service.BookService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<FindBookDTO> createBook(@RequestBody @Valid CreateBookDTO book) {
        log.info("Creating a new book: {}", book);
        
        return ResponseEntity
        		.status(HttpStatus.CREATED)
        		.body(bookService.save(book));
    }

    @GetMapping
    public ResponseEntity<PageDTO<FindBookDTO>> findAll( 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
    	
    	final var pageable = PageRequest.of(page, size);
        
        log.info("Fetching all books with page: {}, size: {}", page, size);
        
        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindBookDTO> findById(@PathVariable(required = true) Long id) {
        log.info("Fetching book with id: {}", id);
        
        final var optionalFindBookDTO = bookService.findById(id);
        
        if (optionalFindBookDTO.isEmpty()) {
            log.error("Book not found with id: {}", id);
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(optionalFindBookDTO.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FindBookDTO> update(
    		@PathVariable(required = true) Long id, 
    		@RequestBody @Valid UpdateBookDTO book) {
        log.info("Updating book with id: {}", id);

        return ResponseEntity.ok(bookService.update(id, book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(required = true) Long id) {
        log.info("Deleting book with id: {}", id);
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageDTO<FindBookDTO>> searchBooks(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        
        final var pageable = PageRequest.of(page, size);
        
        log.info("Searching books with title: {}, author: {}, page: {}, size: {}", title, author, page, size);
        
        return ResponseEntity.ok(bookService.findAll(title, author, pageable));
    }
    
    
    @GetMapping("/{id}/ai-insights")
    public ResponseEntity<String> aiInsight(@PathVariable(required = true) Long id) {
        log.info("Fetching book with id: {}", id);
        
        final var optionalFindBookDTO = bookService.findBookAIInsight(id);
        
        if (optionalFindBookDTO.isEmpty()) {
            log.error("Book not found with id: {}", id);
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(optionalFindBookDTO.get());
    }
}
