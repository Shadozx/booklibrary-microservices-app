package com.shadoww.libraryservice.service.interfaces;

import com.shadoww.api.service.CrudService;

import com.shadoww.libraryservice.model.Book;
import com.shadoww.libraryservice.model.Chapter;

import java.util.List;

public interface ChapterService extends CrudService<Chapter, Long> {


    Chapter getChapterByBookAndNumber(Book book, int number);

    List<Chapter> getBookChapters(Book book);

    void deleteByBook(long id);
}
