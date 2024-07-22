package com.shadoww.api.dto.response.comment;

//import com.shadoww.BookLibraryApp.model.comment.BookComment;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BookCommentResponse {

    long id;

    String text;

    LocalDateTime createdAt;

    long ownerId;

    long bookId;

//    public BookCommentResponse(BookComment comment) {
//
//        this.id = comment.getId();
//        this.text = comment.getText();
//        this.ownerId = comment.getOwner().getId();
//        this.createdAt = comment.getCreatedAt();
//        this.bookId = comment.getBook().getId();
//    }
}
