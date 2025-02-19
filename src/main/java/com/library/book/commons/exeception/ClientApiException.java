package com.library.book.commons.exeception;

public class ClientApiException extends RuntimeException {
	
	private static final long serialVersionUID = -3635465900985495346L;

	public ClientApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
