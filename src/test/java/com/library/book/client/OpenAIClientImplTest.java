package com.library.book.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.library.book.client.impl.OpenAIClientImpl;
import com.library.book.client.impl.OpenAIProperties;
import com.library.book.client.impl.OpenAIRequestBuilder;
import com.library.book.commons.exeception.ClientApiException;

class OpenAIClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OpenAIProperties openAIProperties;

    @InjectMocks
    private OpenAIClientImpl openAIClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(openAIProperties.getUri()).thenReturn("http://openai.com");
        when(openAIProperties.getKey()).thenReturn("openai-key");
    }

    @Test
    @DisplayName("Should return response when API call is successful")
    void shouldReturnResponseWhenApiCallIsSuccessful() {
        // Given
        String prompt = "Test prompt";
        String expectedResponse = "{\"choices\":[{\"text\":\"Mocked response\"}]}";
        var entity = new OpenAIRequestBuilder(openAIProperties).buildRequest(prompt);

        when(restTemplate.exchange(openAIProperties.getUri(), HttpMethod.POST, entity, String.class))
            .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        // When
        String response = openAIClient.generateSummary(prompt);

        // Then
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should throw ClientApiException on client error (400 Bad Request)")
    void shouldThrowClientApiExceptionOnClientError() {
        // Given
        String prompt = "Test prompt";
        var entity = new OpenAIRequestBuilder(openAIProperties).buildRequest(prompt);

        when(restTemplate.exchange(openAIProperties.getUri(), HttpMethod.POST, entity, String.class))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Client error"));

        // When & Then
        ClientApiException exception = assertThrows(ClientApiException.class, () -> openAIClient.generateSummary(prompt));
        assertTrue(exception.getMessage().contains("Client error"));
    }

    @Test
    @DisplayName("Should throw ClientApiException on server error (500 Internal Server Error)")
    void shouldThrowClientApiExceptionOnServerError() {
        // Given
        String prompt = "Test prompt";
        var entity = new OpenAIRequestBuilder(openAIProperties).buildRequest(prompt);

        when(restTemplate.exchange(openAIProperties.getUri(), HttpMethod.POST, entity, String.class))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error"));

        // When & Then
        ClientApiException exception = assertThrows(ClientApiException.class, () -> openAIClient.generateSummary(prompt));
        assertTrue(exception.getMessage().contains("Server error"));
    }
}
