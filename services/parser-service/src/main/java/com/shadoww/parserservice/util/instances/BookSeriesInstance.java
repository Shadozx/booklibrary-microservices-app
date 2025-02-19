package com.shadoww.parserservice.util.instances;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BookSeriesInstance {

    private String title;
//    private String biography;

    private List<BookInstance> books;

    public void addAllBooks(List<BookInstance> books) {
        this.books.addAll(books);
    }
}
