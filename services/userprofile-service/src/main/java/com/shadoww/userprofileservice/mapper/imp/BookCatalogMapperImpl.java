package com.shadoww.userprofileservice.mapper.imp;


import com.shadoww.api.dto.request.BookCatalogRequest;
import com.shadoww.api.dto.response.BookCatalogResponse;
import com.shadoww.userprofileservice.mapper.BookCatalogMapper;
import com.shadoww.userprofileservice.model.BookCatalog;
import org.springframework.stereotype.Component;

@Component
public class BookCatalogMapperImpl implements BookCatalogMapper {

    @Override
    public BookCatalogRequest dtoToRequest(BookCatalog catalog) {
        BookCatalogRequest request = new BookCatalogRequest();

        request.setTitle(catalog.getTitle());
        request.setIsPublic(catalog.getIsPublic());

        return request;
    }

    @Override
    public BookCatalog dtoToModel(BookCatalogRequest request) {
        BookCatalog catalog = new BookCatalog();

        catalog.setId(null);
        catalog.setTitle(request.getTitle());
        catalog.setIsPublic(request.getIsPublic());


        return catalog;
    }

    @Override
    public BookCatalogResponse dtoToResponse(BookCatalog catalog) {

        return new BookCatalogResponse(
                catalog.getId(),
                catalog.getTitle(),
                catalog.getIsPublic(),
                catalog.getOwnerId()
        );
    }

    @Override
    public BookCatalog dtoToModel(BookCatalogResponse response) {
        BookCatalog catalog = new BookCatalog();

        catalog.setId(response.getId());
        catalog.setTitle(response.getTitle());
        catalog.setIsPublic(response.getIsPublic());

        return catalog;
    }
}
