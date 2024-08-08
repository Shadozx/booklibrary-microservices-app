package com.shadoww.api.dto.response.comment;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BookCommentResponse {

    long id;

    String text;

    LocalDateTime createdAt;

    long ownerId;

    long bookId;

}
