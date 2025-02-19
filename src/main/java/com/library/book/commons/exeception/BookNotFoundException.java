package com.library.book.commons.exeception;

public class BookNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BookNotFoundException(Long id) {
		super(String.format("Book with id %d not found", id));
	}
	
}
