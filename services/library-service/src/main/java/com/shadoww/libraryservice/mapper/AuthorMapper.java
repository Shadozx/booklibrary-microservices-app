package com.shadoww.libraryservice.mapper;

import com.shadoww.api.dto.request.AuthorRequest;
import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.libraryservice.model.Author;
import org.mapstruct.Mapper;

@Mapper
public interface AuthorMapper {


    AuthorRequest dtoToRequest(Author author);

    Author dtoToModel(AuthorRequest request);

    AuthorResponse dtoToResponse(Author author);

    Author dtoToModel(AuthorResponse response);

}
