package com.shadoww.userprofileservice.mapper;


import com.shadoww.api.dto.request.BookCatalogRequest;
import com.shadoww.api.dto.response.BookCatalogResponse;
import com.shadoww.userprofileservice.model.BookCatalog;
import org.mapstruct.Mapper;

@Mapper
public interface BookCatalogMapper {


    BookCatalogRequest dtoToRequest(BookCatalog catalog);

    BookCatalog dtoToModel(BookCatalogRequest request);

    BookCatalogResponse dtoToResponse(BookCatalog catalog);

    BookCatalog dtoToModel(BookCatalogResponse response);

}
