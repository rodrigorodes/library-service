package com.library.book.client.impl;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.library.book.client.AIClient;
import com.library.book.commons.exeception.ClientApiException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service("openAIClientImpl")
@Slf4j 
public class OpenAIClientImpl implements AIClient {

    private final RestTemplate restTemplate;
    
    private final OpenAIProperties openAIProperties;

    @Retry(name = "openaiApiRetry", fallbackMethod = "fallbackForRetry")
    @CircuitBreaker(name = "openaiApiCircuitBreaker", fallbackMethod = "fallbackForCircuitBreaker")
    @Override
    public String generateSummary(String prompt) {

        final var entity = new OpenAIRequestBuilder(openAIProperties).buildRequest(prompt);

        try {
            log.info("Sending request to OpenAI API with prompt: {}", prompt);
            ResponseEntity<String> response = restTemplate.exchange(openAIProperties.getUri(), HttpMethod.POST, entity, String.class);
            log.info("Received response from OpenAI API.");
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Client error while calling OpenAI API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ClientApiException("Client error occurred while calling OpenAI API", e);
        } catch (HttpServerErrorException e) {
            log.error("Server error while calling OpenAI API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ClientApiException("Server error occurred while calling OpenAI API", e);
        } catch (Exception e) {
            log.error("Unexpected error while calling OpenAI API: {}", e.getMessage(), e);
            throw new ClientApiException("Unexpected error occurred while calling OpenAI API", e);
        }
    }

    @SuppressWarnings("unused")
	private String fallbackForRetry(String prompt, Throwable ex) {
        log.warn("Retry failed for prompt '{}'. Error: {}", prompt, ex.getMessage());
        return "Retry failed due to: " + ex.getMessage();
    }

    @SuppressWarnings("unused")
	private String fallbackForCircuitBreaker(String prompt, Throwable ex) {
        log.warn("Circuit breaker is open for prompt '{}'. Error: {}", prompt, ex.getMessage());
        return "Our service is down for scheduled maintenance. Thank you for your understanding.";
    }
}
