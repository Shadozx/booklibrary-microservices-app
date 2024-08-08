package com.shadoww.ratingservice.mapper.impl;

import com.shadoww.api.dto.request.BookRatingRequest;
import com.shadoww.api.dto.response.BookRatingResponse;
import com.shadoww.ratingservice.mapper.BookRatingMapper;
import com.shadoww.ratingservice.model.BookRating;
import org.springframework.stereotype.Component;

@Component
public class BookRatingMapperImpl implements BookRatingMapper {
    @Override
    public BookRatingRequest dtoToRequest(BookRating rating) {
        BookRatingRequest request = new BookRatingRequest();

        request.setRating(rating.getRating());
        request.setBookId(rating.getBookId());

        return request;
    }

    @Override
    public BookRating dtoToModel(BookRatingRequest request) {
        BookRating rating = new BookRating();

        rating.setId(null);
        rating.setRating(request.getRating());
        rating.setBookId(request.getBookId());

        return rating;
    }

    @Override
    public BookRatingResponse dtoToResponse(BookRating rating) {
        return new BookRatingResponse(
                rating.getId(),
                rating.getRating(),
                rating.getBookId(),
                rating.getOwnerId()
        );
    }

    @Override
    public BookRating dtoToModel(BookRatingResponse response) {
        BookRating rating = new BookRating();

        rating.setId(response.getId());
        rating.setRating(response.getRating());
        rating.setBookId(response.getBookId());
        rating.setOwnerId(response.getOwnerId());

        return rating;
    }
}