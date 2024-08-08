package com.shadoww.ratingservice.service;

import com.shadoww.api.service.CrudService;
import com.shadoww.ratingservice.model.BookRating;

import java.util.List;


public interface BookRatingService extends CrudService<BookRating, Long> {

    List<BookRating> getBookRatings(long bookId);

    BookRating getOwnerBookRating(long ownerId, long bookId);
}