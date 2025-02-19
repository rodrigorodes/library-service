package com.library.book.dto;

import com.library.book.commons.validators.YearLessThanOrEqualToCurrent;
import com.library.book.entity.Book;

import jakarta.validation.constraints.NotNull;

public record UpdateBookDTO(

		String title, 
		String author, 
		String isbn, 
	  @NotNull	@YearLessThanOrEqualToCurrent Integer  publicationYear,
		String description) {

	public Book toModel() {
		return Book.of(null, title, author, isbn, publicationYear, description);
	}

}
