package com.shadoww.libraryservice.service.interfaces;

import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.service.CrudService;
import com.shadoww.libraryservice.model.Author;
import com.shadoww.libraryservice.model.Book;
import com.shadoww.libraryservice.model.BookSeries;

import java.util.List;


public interface BookService extends CrudService<Book, Long> {

//    Book getByUrl(String url);

    Book getByTitle(String title);
    List<Book> getBySearchTitle(String title);

    boolean existByTitle(String title);

    List<Book> filterBooks(BookFilterRequest filterRequest);

    List<Book> getBookSeriesBooks(BookSeries bookSeries);

    List<Book> getAuthorBooks(Author author);
}