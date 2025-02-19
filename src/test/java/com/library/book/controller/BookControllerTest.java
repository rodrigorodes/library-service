package com.library.book.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.library.book.client.impl.OpenAIClientImpl;
import com.library.book.repository.BookRepository;
import com.library.book.service.BookService;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/test-data.sql")  
class BookControllerTest {

    private static final String BOOKS_URI = "/api/v1/books";
	private static final String BOOKS_URI_ID = String.format("%s/{id}", BOOKS_URI);
	private static final String BOOKS_URI_SEARCH = String.format("%s/search", BOOKS_URI);

	@Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private OpenAIClientImpl openAIClient;

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookRepository bookRepository;

    @Test
    @DisplayName("Should create a new book successfully")
    void testCreateBook() throws Exception {
    	
        final String bookJson = bookJson();

        mockMvc.perform(MockMvcRequestBuilders.post(BOOKS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated())  
                .andExpect(jsonPath("$.title").value("Spring Boot Essentials"))  
                .andExpect(jsonPath("$.author").value("John Doe"))  
                .andExpect(jsonPath("$.isbn").value("AAAAAAAAAA"))  
                .andExpect(jsonPath("$.publicationYear").value(2005))  
                .andExpect(jsonPath("$.description").value("A comprehensive guide to Spring Boot"));  
    }

	private String bookJson() {
		final String bookJson = "{"
                + "\"title\": \"Spring Boot Essentials\","
                + "\"author\": \"John Doe\","
                + "\"isbn\": \"AAAAAAAAAA\","
                + "\"publicationYear\": 2005,"
                + "\"description\": \"A comprehensive guide to Spring Boot\""
                + "}";
		return bookJson;
	}

    @Test
    @DisplayName("Should return all books with pagination")
    void testFindAllBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BOOKS_URI)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(10))
                .andExpect(jsonPath("$.content[0].title").value("Spring Boot Essentials"))
                .andExpect(jsonPath("$.content[0].author").value("John Doe"));
    }

    @Test
    @DisplayName("Should return 404 when book is not found by ID")
    void testFindBookByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BOOKS_URI_ID, 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should find a book by its ID")
    void testFindBookById() throws Exception {
    	
        mockMvc.perform(MockMvcRequestBuilders.get(BOOKS_URI_ID, 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Spring Boot Essentials"))
                .andExpect(jsonPath("$.author").value("John Doe"));
    }

    @Test
    @DisplayName("Should update an existing book successfully")
    void testUpdateBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(BOOKS_URI_ID, 20L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Updated Spring Boot\", \"author\": \"Jane Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Spring Boot"))
                .andExpect(jsonPath("$.author").value("Jane Doe"));
    }

    @Test
    @DisplayName("Should delete a book by its ID successfully")
    void testDeleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete(BOOKS_URI_ID, 10L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when trying to delete a non-existent book")
    void testDeleteBookNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BOOKS_URI_ID, 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should search for books using title and author filters")
    void testSearchBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BOOKS_URI_SEARCH)
                .param("title", "Spring")
                .param("author", "John")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Spring Boot Essentials"))
                .andExpect(jsonPath("$.content[0].author").value("John Doe"));
    }
    
    
    @Test
    @DisplayName("Should find Summary book by its ID")
    void testFindSummaryBookById() throws Exception {
    	
        mockMvc.perform(MockMvcRequestBuilders.get(BOOKS_URI_ID, 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Spring Boot Essentials"))
                .andExpect(jsonPath("$.author").value("John Doe"));
    }
}
