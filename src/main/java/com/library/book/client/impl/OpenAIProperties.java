package com.library.book.client.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties("openai.api")
@Configuration
@Getter
@Setter
public class OpenAIProperties {

	private String key;
	
	private String uri;
}
