package com.library.book.entity;



import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@AllArgsConstructor(staticName = "of")

@Setter
@Getter
@Table
@Entity
public class Book {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @NotNull
    @Size(min = 1, max = 255)
    private String author;

    @NotNull
    @Size(min = 10, max = 13)
    private String isbn;

    @NotNull
    @Column(name = "publication_year")
    private Integer publicationYear;

    @NotNull
    @Size(max = 1000)
    private String description;
    
    @Deprecated
    public Book(){}
    

    public Book update(String title, String author, String isbn, Integer publicationYear ) {
    	this.title = Optional.ofNullable(title).orElse(this.title);   
    	this.author = Optional.ofNullable(author).orElse(this.author);    	
    	this.isbn = Optional.ofNullable(isbn).orElse(this.isbn);    	
    	this.publicationYear = Optional.ofNullable(publicationYear).orElse(this.publicationYear);  
    	this.description = Optional.ofNullable(description).orElse(this.description);  
    	return this;
    }
}