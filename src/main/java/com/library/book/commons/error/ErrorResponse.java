package com.library.book.commons.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private final String message;

    private final String field;

    private final Object parameter;
}
