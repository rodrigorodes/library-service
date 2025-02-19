package com.library.book.dto;

import com.library.book.commons.validators.YearLessThanOrEqualToCurrent;
import com.library.book.entity.Book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBookDTO(

		@NotBlank @Size(min = 1, max = 255) String title,
		@NotBlank @Size(min = 1, max = 255) String author,
		@NotBlank @Size(min = 10, max = 13) String isbn, 
		@NotNull @YearLessThanOrEqualToCurrent Integer publicationYear,
		@NotBlank String description) {

	public Book toModel() {
		return Book.of(null, title, author, isbn, publicationYear, description);
	}

}
