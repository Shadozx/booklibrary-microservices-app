package com.shadoww.bookservice.mapper.imp;

import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.bookservice.mapper.BookMapper;
import com.shadoww.bookservice.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookRequest dtoToRequest(Book book) {
        BookRequest request = new BookRequest();
        request.setTitle(book.getTitle());
        request.setDescription(book.getDescription());
        request.setImageId(book.getBookImageId());

        return request;
    }

    @Override
    public Book dtoToModel(BookRequest request) {
        Book book = new Book();

        book.setTitle(request.getTitle());
        book.setDescription(request.getDescription());
        book.setBookImageId(request.getImageId());

        return book;
    }

    @Override
    public BookResponse dtoToResponse(Book book) {

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getAmount(),
                book.getBookImageId());
//                book.getUploadedUrl());
    }

    @Override
    public Book dtoToModel(BookResponse response) {
        Book book = new Book();

        book.setTitle(response.getTitle());
        book.setDescription(response.getDescription());
        book.setBookImageId(response.getImageId());

        return book;
    }
}
