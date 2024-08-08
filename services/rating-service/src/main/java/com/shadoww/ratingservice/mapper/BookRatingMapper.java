package com.shadoww.ratingservice.mapper;

import com.shadoww.api.dto.request.BookRatingRequest;
import com.shadoww.api.dto.response.BookRatingResponse;
import com.shadoww.ratingservice.model.BookRating;

public interface BookRatingMapper {

    BookRatingRequest dtoToRequest(BookRating rating);

    BookRating dtoToModel(BookRatingRequest request);

    BookRatingResponse dtoToResponse(BookRating rating);
    //
    BookRating dtoToModel(BookRatingResponse response);
}
