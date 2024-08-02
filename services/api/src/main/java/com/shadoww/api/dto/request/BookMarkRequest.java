package com.shadoww.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BookMarkRequest {

    private long bookId;
    private long chapterId;
    private long catalogId;
    private long ownerId;
    private int paragraph;
}