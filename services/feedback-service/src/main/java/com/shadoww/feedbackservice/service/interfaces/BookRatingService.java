package com.shadoww.feedbackservice.service.interfaces;

import com.shadoww.api.service.CrudService;
import com.shadoww.feedbackservice.model.rating.BookRating;

import java.util.List;


public interface BookRatingService extends CrudService<BookRating, Long> {

    List<BookRating> getBookRatings(long bookId);

    BookRating getOwnerBookRating(long ownerId, long bookId);
}