package com.shadoww.bookservice.mapper;

import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.bookservice.model.Book;
import org.mapstruct.Mapper;

//@Mapper
public interface BookMapper {

//    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookRequest dtoToRequest(Book book);

    Book dtoToModel(BookRequest request);

    BookResponse dtoToResponse(Book book);

    Book dtoToModel(BookResponse response);

}
