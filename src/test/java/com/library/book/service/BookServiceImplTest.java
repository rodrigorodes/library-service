package com.library.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.library.book.client.AIClient;
import com.library.book.commons.exeception.BookNotFoundException;
import com.library.book.dto.CreateBookDTO;
import com.library.book.dto.FindBookDTO;
import com.library.book.dto.PageDTO;
import com.library.book.dto.UpdateBookDTO;
import com.library.book.entity.Book;
import com.library.book.repository.BookRepository;
import com.library.book.service.impl.BookServiceImpl;

class BookServiceImplTest {

    @Mock
    private BookRepository repository;

    @Mock
    private AIClient aIClient;

    @InjectMocks
    private BookServiceImpl bookService;

    private CreateBookDTO createBookDTO;
    private UpdateBookDTO updateBookDTO;
    private FindBookDTO findBookDTO;

	private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
       
        // Entity
        book = Book.of(1L,"Book Title", "Author Name", "1234567890", 2025, "DESC");

        // Mock DTOs
        createBookDTO = new CreateBookDTO("Book Title", "Author Name", "1234567890", 2025, null);
        updateBookDTO = new UpdateBookDTO("Updated Title", "Updated Author", "0987654321", 2026, null);
        findBookDTO = new FindBookDTO(1L, "Book Title", "Author Name", "1234567890", 2025, null);
    }

    @Test
    @DisplayName("Test saving a new book")
    void testSave() {
        when(repository.save(any())).thenReturn(book);

        FindBookDTO savedBook = bookService.save(createBookDTO);

        assertNotNull(savedBook);
        assertEquals(savedBook.title(), createBookDTO.title());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test fetching all books")
    void testFindAll() {
        Page<Book> page = new PageImpl<>(List.of(book));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        PageDTO<FindBookDTO> result = bookService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(result.getContent().size(), 1);
        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Test fetching book by id")
    void testFindById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));

        Optional<FindBookDTO> result = bookService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(result.get().title(), findBookDTO.title());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Test updating a book")
    void testUpdate() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));
        when(repository.save(any())).thenReturn(book);

        FindBookDTO updatedBook = bookService.update(1L, updateBookDTO);

        assertNotNull(updatedBook);
        assertEquals(updatedBook.title(), updateBookDTO.title());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test deleting a book")
    void testDelete() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));

        bookService.deleteById(1L);

        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test handling book not found for update")
    void testUpdateBookNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.update(1L, updateBookDTO));

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Test fetching AI insight for a book")
    void testFindBookAIInsight() {
    	
        when(repository.findById(anyLong())).thenReturn(Optional.of(book));
        
        when(aIClient.generateSummary(any())).thenReturn("AI Insight");

        Optional<String> result = bookService.findBookAIInsight(1L);

        assertTrue(result.isPresent());
        assertEquals(result.get(), "AI Insight");
        verify(repository, times(1)).findById(anyLong());
        verify(aIClient, times(1)).generateSummary(any());
    }
}
