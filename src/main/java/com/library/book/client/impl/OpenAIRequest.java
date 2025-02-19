package com.library.book.client.impl;

import java.util.List;

public record OpenAIRequest(String model, List<Message> messages) {
	
    public OpenAIRequest(String prompt) {
    	
        this("gpt-3.5-turbo", List.of(
            new Message("system", "You are a helpful assistant."),
            new Message("user", prompt)
        ));
    }
}
