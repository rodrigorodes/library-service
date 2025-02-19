package com.library.book.dto;

import com.library.book.entity.Book;

public record FindBookDTO(	
		 long id,
		 String title, 
		 String author, 
		 String isbn, 
		 Integer publicationYear,
		 String description) {

	
	public static FindBookDTO of(Book book) {
		return new FindBookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublicationYear(), book.getDescription());
	}
}
