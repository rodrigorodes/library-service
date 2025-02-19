package com.library.book.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.library.book.client.AIClient;
import com.library.book.commons.exeception.BookNotFoundException;
import com.library.book.dto.CreateBookDTO;
import com.library.book.dto.FindBookDTO;
import com.library.book.dto.PageDTO;
import com.library.book.dto.UpdateBookDTO;
import com.library.book.repository.BookRepository;
import com.library.book.repository.specification.BookSpecifications;
import com.library.book.service.BookService;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final AIClient aIClient;

    @Override
    public FindBookDTO save(CreateBookDTO createBookDTO) {
    	
        log.info("Saving a new book: {}", createBookDTO);
        
        return FindBookDTO.of(repository.save(createBookDTO.toModel()));
    }

    @Override
    public PageDTO<FindBookDTO> findAll(Pageable pageable) {
        log.info("Fetching all books with pageable: {}", pageable);
        var bookDTOPage = repository.findAll(pageable).map(FindBookDTO::of);
        return new PageDTO<>(
                bookDTOPage.getContent(),
                bookDTOPage.getNumber(),
                bookDTOPage.getSize(),
                bookDTOPage.getTotalPages()
        );
    }

    @Override
    public Optional<FindBookDTO> findById(Long id) {
        log.info("Fetching book with id: {}", id);
        return repository.findById(id).map(FindBookDTO::of);
    }

    @Override
    public FindBookDTO update(Long id, UpdateBookDTO updateBookDTO) {
        log.info("Updating book with id: {}", id);

        var existingBook = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with id: {}", id);
                    return new BookNotFoundException(id);
                });

        var updatedBook = existingBook.update(
                updateBookDTO.title(),
                updateBookDTO.author(),
                updateBookDTO.isbn(),
                updateBookDTO.publicationYear()
        );

        log.info("Book updated successfully: {}", updatedBook);
        return FindBookDTO.of(repository.save(updatedBook));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting book with id: {}", id);

        var book = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with id: {}", id);
                    return new BookNotFoundException(id);
                });

        repository.deleteById(book.getId());
        log.info("Book deleted successfully: {}", book);
    }

    @Override
    public PageDTO<FindBookDTO> findAll(String title, String author, Pageable pageable) {
        log.info("Searching books with title: {}, author: {}", title, author);

        var bookSpec = Specification
                .where(BookSpecifications.hasTitle(title))
                .and(BookSpecifications.hasAuthor(author));

        var bookDTOPage = repository.findAll(bookSpec, pageable).map(FindBookDTO::of);

        return new PageDTO<>(
                bookDTOPage.getContent(),
                bookDTOPage.getNumber(),
                bookDTOPage.getSize(),
                bookDTOPage.getTotalPages()
        );
    }

    @Override
    public Optional<String> findBookAIInsight(@NotNull Long id) {
        log.info("Fetching AI insight for book with id: {}", id);
        return repository.findById(id)
                .map(book -> aIClient.generateSummary(book.getDescription()));
    }
}
