package com.library.book.client.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class OpenAIRequestBuilder {

    private final OpenAIProperties openAIProperties;

    public OpenAIRequestBuilder(OpenAIProperties openAIProperties) {
        this.openAIProperties = openAIProperties;
    }

    public HttpEntity<OpenAIRequest> buildRequest(String prompt) {
        return new HttpEntity<>(new OpenAIRequest(prompt), createHeaders());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAIProperties.getKey());
        return headers;
    }
}
