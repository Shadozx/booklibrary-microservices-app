package com.shadoww.authorservice.mapper.imp;

import com.shadoww.api.dto.request.AuthorRequest;

import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.authorservice.mapper.AuthorMapper;
import com.shadoww.authorservice.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public AuthorRequest dtoToRequest(Author author) {
        AuthorRequest request = new AuthorRequest();
        request.setName(author.getName());
        request.setBiography(author.getBiography());

        return request;
    }

    @Override
    public Author dtoToModel(AuthorRequest request) {
        Author author = new Author();

        author.setName(request.getName());
        author.setBiography(request.getBiography());

        return author;
    }

    @Override
    public AuthorResponse dtoToResponse(Author author) {

        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getBiography(),
                author.getUploadedUrl());
    }

    @Override
    public Author dtoToModel(AuthorResponse response) {
        Author author = new Author();

        author.setId(response.getId());
        author.setName(response.getName());
        author.setBiography(response.getBiography());

        return author;
    }
}
