package com.shadoww.authorservice.mapper;

import com.shadoww.api.dto.request.AuthorRequest;

import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.authorservice.model.Author;
import org.mapstruct.Mapper;

@Mapper
public interface AuthorMapper {

//    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorRequest dtoToRequest(Author author);

    Author dtoToModel(AuthorRequest request);

    AuthorResponse dtoToResponse(Author author);

    Author dtoToModel(AuthorResponse response);

}
