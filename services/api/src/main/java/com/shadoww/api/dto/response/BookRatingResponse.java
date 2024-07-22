package com.shadoww.api.dto.response;

//import com.shadoww.BookLibraryApp.model.BookRating;
import lombok.Value;

@Value
public class BookRatingResponse {

    long id;

    long rating;

    long bookId;

    long ownerId;

//    public BookRatingResponse(BookRating rating) {
//        this.id = rating.getId();
//        this.rating = rating.getRating();
//        this.bookId = rating.getBook().getId();
//        this.ownerId = rating.getOwner().getId();
//    }
}
