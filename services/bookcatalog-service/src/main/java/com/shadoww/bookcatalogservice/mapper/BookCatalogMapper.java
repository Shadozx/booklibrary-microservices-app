package com.shadoww.bookcatalogservice.mapper;


import com.shadoww.api.dto.request.BookCatalogRequest;
import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.api.dto.response.BookCatalogResponse;
import com.shadoww.bookcatalogservice.model.BookCatalog;
import org.mapstruct.Mapper;

@Mapper
public interface BookCatalogMapper {


    BookCatalogRequest dtoToRequest(BookCatalog catalog);

    BookCatalog dtoToModel(BookCatalogRequest request);

    BookCatalogResponse dtoToResponse(BookCatalog catalog);

    BookCatalog dtoToModel(BookCatalogResponse response);

}
