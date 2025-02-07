package com.shadoww.userprofileservice.mapper.imp;

import com.shadoww.api.dto.request.BookMarkRequest;
import com.shadoww.api.dto.response.BookMarkResponse;
import com.shadoww.userprofileservice.mapper.BookMarkMapper;
import com.shadoww.userprofileservice.model.BookMark;
import com.shadoww.userprofileservice.service.interfac.BookCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookMarkMapperImpl implements BookMarkMapper {
    private final BookCatalogService bookCatalogService;

    @Autowired
    public BookMarkMapperImpl(BookCatalogService bookCatalogService) {
        this.bookCatalogService = bookCatalogService;
    }

    @Override
    public BookMarkRequest dtoToRequest(BookMark mark) {
        BookMarkRequest request = new BookMarkRequest();

        request.setBookId(mark.getBookId());
        request.setChapterId(mark.getChapterId());
        request.setChapterId(mark.getChapterId());
        request.setCatalogId(mark.getCatalog().getId());
        request.setOwnerId(mark.getOwnerId());

        return request;
    }

    @Override
    public BookMark dtoToModel(BookMarkRequest request) {
        return getBookMark(
                request.getBookId(),
                request.getCatalogId(),
                request.getOwnerId(),
                request.getParagraph()
        );
    }

    @Override
    public BookMarkResponse dtoToResponse(BookMark mark) {
        return new BookMarkResponse(
                mark.getId(),
                mark.getParagraph(),
                mark.getCatalog().getId(),
                mark.getBookId(),
                mark.getChapterId(),
                mark.getOwnerId()
        );
    }

    @Override
    public BookMark dtoToModel(BookMarkResponse response) {

        return getBookMark(
                response.getBookId(),
                response.getCatalogId(),
                response.getOwnerId(),
                response.getParagraph()
        );
    }

    private BookMark getBookMark(long bookId,
                                 long catalogId,
                                 long ownerId,
                                 int paragraph) {
        BookMark mark = new BookMark();

        mark.setId(null);
        mark.setBookId(bookId);
        mark.setCatalog(bookCatalogService.readById(catalogId));
        mark.setOwnerId(ownerId);
        mark.setParagraph(paragraph);

        return mark;
    }
}
