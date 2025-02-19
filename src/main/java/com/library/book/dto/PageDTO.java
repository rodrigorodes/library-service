package com.library.book.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageDTO<T> {

    private List<T> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer total;

    public Boolean isLast() {
        return this.pageNumber +1 >= this.totalPages();
    }

    public Integer totalPages() {
        if (this.content.isEmpty() && this.total == 0) {
            return 1;
        } else {
            return Double.valueOf(Math.ceil(this.total / this.pageSize)).intValue();
        }
    }

    public Integer size() {
        return this.content.size();
    }
 
}