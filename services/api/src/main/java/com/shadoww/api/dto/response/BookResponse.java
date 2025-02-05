package com.shadoww.api.dto.response;


import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Setter
@Getter
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookResponse {

    long id;

    String title;

    String description;

    long amount;

    Long imageId;

//    String uploadedUrl;

//    public BookResponse(Book book) {
//        this.id = book.getId();
//        this.title = book.getTitle();
//        this.amount = book.getAmount();
//        this.description = book.getDescription();
//
//        BookImage bookImage = book.getBookImage();
//
//
//        this.bookImageUrl = bookImage == null ? null : bookImage.getUrl();
//        this.uploadedUrl = book.getUploadedUrl();
//    }


    public BookResponse(long id, String title, String description, long amount, Long imageId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.imageId = imageId;
//        this.uploadedUrl = uploadedUrl;
    }
}
