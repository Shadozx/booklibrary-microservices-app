package com.shadoww.authorservice.service.interfaces;

import com.shadoww.authorservice.model.Author;
import com.shadoww.api.service.CrudService;

import java.util.List;

public interface AuthorService extends CrudService<Author, Long> {

    Author readByName(String name);


    boolean existsByName(String name);
    boolean existsByUrl(String url);

//    List<Author> getBookAuthors(Long bookId);
}
