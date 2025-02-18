package com.shadoww.libraryservice.service.interfaces;

import com.shadoww.libraryservice.model.BookSeries;
import com.shadoww.api.service.CrudService;

import java.util.List;


public interface BookSeriesService extends CrudService<BookSeries, Long> {

    BookSeries readByTitle(String title) ;

    boolean existsByTitle(String title);


    List<BookSeries> getBookSeriesByBook(Long bookId);
}