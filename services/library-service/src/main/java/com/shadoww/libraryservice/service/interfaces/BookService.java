package com.shadoww.libraryservice.service.interfaces;

//import com.shadoww.bookLibrary.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.libraryservice.model.Book;

import com.shadoww.api.service.CrudService;

import java.util.List;


public interface BookService extends CrudService<Book, Long> {

//    Book getByUrl(String url);

    Book getByTitle(String title);
    List<Book> getBySearchTitle(String title);

    boolean existByTitle(String title);

//    boolean existsByUrl(String uploadedUrl);

    List<Book> filterBooks(BookFilterRequest filterRequest);
}