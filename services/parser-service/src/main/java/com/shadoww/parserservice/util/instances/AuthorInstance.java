package com.shadoww.parserservice.util.instances;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AuthorInstance {

    private String name;
    private String biography;

    private List<BookInstance> books;

    public AuthorInstance() {
        books = new ArrayList<>();
    }

    public void addAllBooks(List<BookInstance> books) {
        this.books.addAll(books);
    }

    @Override
    public String toString() {
        return "AuthorInstance{" +
                "name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}
