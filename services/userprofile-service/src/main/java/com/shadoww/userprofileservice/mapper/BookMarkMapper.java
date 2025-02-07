package com.shadoww.userprofileservice.mapper;


import com.shadoww.api.dto.request.BookMarkRequest;
import com.shadoww.api.dto.response.BookMarkResponse;
import com.shadoww.userprofileservice.model.BookMark;
import org.mapstruct.Mapper;

@Mapper
public interface BookMarkMapper {


    BookMarkRequest dtoToRequest(BookMark mark);

    BookMark dtoToModel(BookMarkRequest request);

    BookMarkResponse dtoToResponse(BookMark mark);

    BookMark dtoToModel(BookMarkResponse response);

}
