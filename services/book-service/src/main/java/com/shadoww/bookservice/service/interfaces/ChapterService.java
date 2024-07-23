package com.shadoww.bookservice.service.interfaces;

import com.shadoww.api.service.CrudService;

import com.shadoww.bookservice.model.Book;
import com.shadoww.bookservice.model.Chapter;

import java.util.List;

public interface ChapterService extends CrudService<Chapter, Long> {


    Chapter getChapterByBookAndNumber(Book book, int number);

    List<Chapter> getBookChapters(Book book);

    void deleteByBook(long id);
}
