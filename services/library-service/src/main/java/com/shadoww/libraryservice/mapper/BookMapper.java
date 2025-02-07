package com.shadoww.libraryservice.mapper;

import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.libraryservice.model.Book;

//@Mapper
public interface BookMapper {

//    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookRequest dtoToRequest(Book book);

    Book dtoToModel(BookRequest request);

    BookResponse dtoToResponse(Book book);

    Book dtoToModel(BookResponse response);

}
