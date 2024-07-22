package com.shadoww.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BookRatingRequest {

    private int rating;
    private long bookId;

}
