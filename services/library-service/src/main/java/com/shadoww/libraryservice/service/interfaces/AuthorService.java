package com.shadoww.libraryservice.service.interfaces;

import com.shadoww.api.service.CrudService;
import com.shadoww.libraryservice.model.Author;

public interface AuthorService extends CrudService<Author, Long> {

    Author readByName(String name);


    boolean existsByName(String name);
//    boolean existsByUrl(String url);

//    List<Author> getBookAuthors(Long bookId);
}
