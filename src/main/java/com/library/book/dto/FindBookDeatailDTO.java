package com.library.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FindBookDeatailDTO {
	
	private final String title;
	private final String author;
	private final String isbn; 
	private final Integer publicationYear;
	private final String summary;
	
}
