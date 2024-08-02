package com.shadoww.bookcatalogservice.controller;


import com.shadoww.api.dto.request.BookCatalogRequest;
//import com.shadoww.bookcatalogservice.model.BookCatalog;
//import com.shadoww.BookLibraryApp.model.user.Person;
//import com.shadoww.bookcatalogservice.service.interfac.BookCatalogService;
//import com.shadoww.bookcatalogservice.service.interfac.PersonService;
//import com.shadoww.BookLibraryApp.util.responsers.*;
import com.shadoww.api.exception.NotFoundException;
import com.shadoww.bookcatalogservice.mapper.BookCatalogMapper;
import com.shadoww.bookcatalogservice.model.BookCatalog;
import com.shadoww.bookcatalogservice.service.interfac.BookCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/users/{userId}/catalogs")
public class ApiCatalogsController {

    private final BookCatalogService bookCatalogService;

    private final BookCatalogMapper bookCatalogMapper;

    @Autowired
    public ApiCatalogsController(
            BookCatalogService bookCatalogService, BookCatalogMapper bookCatalogMapper) {
        this.bookCatalogService = bookCatalogService;
        this.bookCatalogMapper = bookCatalogMapper;
    }

    @GetMapping
    public ResponseEntity<?> getBookCatalogsByUser(@PathVariable long userId) {

        return ResponseEntity.ok(
                bookCatalogService.getByPerson(userId)
                        .stream()
//                        .map(BookCatalogResponse::new)
                        .map(bookCatalogMapper::dtoToResponse)
                        .toList());
    }

    //
//    @PreAuthorize("#userId == principal.id")
    @PostMapping
    public ResponseEntity<?> addCatalog(@PathVariable long userId,
                                        @RequestBody BookCatalogRequest bookCatalogRequest) {

        if (bookCatalogRequest.isTitleEmpty()) {
            return noContent("Назва каталогу не повинна бути пустою");
        }


        BookCatalog catalog = bookCatalogMapper.dtoToModel(bookCatalogRequest);// = new BookCatalog(bookCatalogRequest);

        catalog.setOwnerId(userId);

        System.out.println(catalog);


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookCatalogService.create(catalog));
    }

    @GetMapping("/{catalogId}")
    public ResponseEntity<?> getCatalog(@PathVariable long userId,
                                        @PathVariable long catalogId) {

        BookCatalog catalog = bookCatalogService.readById(catalogId);

        if (userId != catalog.getOwnerId()) {
            throw new NotFoundException("Користувач не має такого каталогу");
        }

        return ResponseEntity.ok(bookCatalogMapper.dtoToResponse(catalog));
    }

    //    @PreAuthorize("#userId == principal.id")
    @PutMapping("/{catalogId}")
    public ResponseEntity<?> updateCatalog(@PathVariable long userId,
                                           @PathVariable long catalogId,
                                           @RequestBody BookCatalogRequest bookCatalogRequest) {

//        if (bookCatalogRequest.isTitleEmpty()) return ResponseCatalog.noContent();

        BookCatalog catalog = bookCatalogService.getByIdAndPerson(catalogId, userId);

        catalog.setTitle(bookCatalogRequest.getTitle());
        catalog.setIsPublic(bookCatalogRequest.getIsPublic());


        return ResponseEntity
                .ok(bookCatalogMapper
                        .dtoToResponse(bookCatalogService.update(catalog))
                );
    }

    @DeleteMapping("/{catalogId}")
    public ResponseEntity<?> deleteCatalog(@PathVariable long userId,
                                           @PathVariable long catalogId) {

        BookCatalog catalog = bookCatalogService.getByIdAndPerson(catalogId, userId);

        bookCatalogService.deleteById(catalog.getId());

        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> noContent(String message) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }

}
