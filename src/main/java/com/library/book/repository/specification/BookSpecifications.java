package com.library.book.repository.specification;


import org.springframework.data.jpa.domain.Specification;

import com.library.book.entity.Book;

public class BookSpecifications {
	
	   public static Specification<Book> hasTitle(String title) {
	        return (root, query, criteriaBuilder) -> 
	            title == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%%" + title.toLowerCase() + "%%");
	    }

	    public static Specification<Book> hasAuthor(String author) {
	        return (root, query, criteriaBuilder) -> 
	            author == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%%" + author.toLowerCase() + "%%");
	    }

}
