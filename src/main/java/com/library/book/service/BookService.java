package com.library.book.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.library.book.dto.CreateBookDTO;
import com.library.book.dto.FindBookDTO;
import com.library.book.dto.PageDTO;
import com.library.book.dto.UpdateBookDTO;

import jakarta.validation.constraints.NotNull;

public interface BookService {

	FindBookDTO save(CreateBookDTO createBookDTO);

	PageDTO<FindBookDTO> findAll(Pageable pageable);

	Optional<FindBookDTO> findById(Long id);

	FindBookDTO update(Long id, UpdateBookDTO updateBookDTO);

	void deleteById(Long id);

	PageDTO<FindBookDTO> findAll(String title, String author, Pageable pageable);

	Optional<String> findBookAIInsight(@NotNull Long id);

}
