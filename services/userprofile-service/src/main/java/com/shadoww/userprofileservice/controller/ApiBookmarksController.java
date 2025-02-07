package com.shadoww.userprofileservice.controller;


import com.shadoww.api.dto.request.BookMarkRequest;
//import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.userprofileservice.mapper.BookMarkMapper;
import com.shadoww.userprofileservice.model.BookCatalog;
import com.shadoww.userprofileservice.model.BookMark;
//import com.shadoww.BookLibraryApp.model.Chapter;
//import com.shadoww.BookLibraryApp.model.user.Person;
//import com.shadoww.BookLibraryApp.security.AuthUser;
import com.shadoww.userprofileservice.service.interfac.BookCatalogService;
import com.shadoww.userprofileservice.service.interfac.BookMarkService;
//import com.shadoww.BookLibraryApp.service.interfaces.BookService;
//import com.shadoww.BookLibraryApp.service.interfaces.ChapterService;
//import com.shadoww.BookLibraryApp.util.responsers.ResponseBookMark;
//import com.shadoww.BookLibraryApp.util.responsers.ResponseUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

//@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/catalogs/{catalogId}/bookmarks")
public class ApiBookmarksController {
    private final BookCatalogService bookCatalogService;
    private final BookMarkService bookMarkService;
    private final BookMarkMapper bookMarkMapper;

    @Autowired
    public ApiBookmarksController(BookCatalogService bookCatalogService,
                                  BookMarkService bookMarkService,
                                  BookMarkMapper bookMarkMapper) {

        this.bookCatalogService = bookCatalogService;
        this.bookMarkService = bookMarkService;
        this.bookMarkMapper = bookMarkMapper;
    }

    @GetMapping
    public ResponseEntity<?> getBookMarksByCatalog(@PathVariable long catalogId) {
        BookCatalog catalog = bookCatalogService.readById(catalogId);

        return ResponseEntity
                .ok(
                        catalog
                                .getBookMarks()
                                .stream()
                                .map(bookMarkMapper::dtoToResponse)
                                .toList()
                );
    }

    @PostMapping
    public ResponseEntity<?> addBookMark(@PathVariable long catalogId,
                                         @RequestBody BookMarkRequest request) {

//        AuthUser authUser = (AuthUser) authentication.getPrincipal();
//        Person owner = authUser.getPerson();

        BookCatalog catalog = bookCatalogService.readById(catalogId);


//        if (owner.getId() != catalog.getOwner().getId()) {
//            return ResponseUser.noFound();
//        }

//        Book book = bookService.readById(request.getBookId());

        BookMark bookMark = null;
        try {
            bookMark = bookMarkService.getBookMarkByCatalogAndBook(request.getBookId(),
                    request.getOwnerId());

        } catch (EntityNotFoundException e) {

            System.out.println("Перевіряємо чи є в користувача з цією книгою закладка");
        }

        if (bookMark == null) {
            bookMark = new BookMark();

        }

        bookMark.setCatalog(catalog);
        bookMark.setBookId(request.getBookId());
        bookMark.setOwnerId(request.getOwnerId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookMarkMapper.dtoToResponse(bookMarkService.create(bookMark)));
    }

//        @GetMapping
//    public ResponseEntity<?> getBookMarkByBookAndCatalog(
//                                                         @PathVariable int catalogId,
//                                                         @RequestBody BookMarkRequest request) {
//        long bookId = request.getBookId();
//
//        Person owner = (Person) authentication.getPrincipal();
//        Book book = bookService.readById(bookId);
//
//    }

    @GetMapping("/{markId}")
    public ResponseEntity<?> getBookMark(@PathVariable long catalogId,
                                         @PathVariable long markId) {

        bookCatalogService.readById(catalogId);

        BookMark mark = bookMarkService.readById(markId);


        return ResponseEntity.ok(bookMarkMapper.dtoToResponse(mark));
    }

    @PutMapping("/{markId}")
    public ResponseEntity<?> updateBookMark(
            @PathVariable long catalogId,
            @PathVariable long markId,
            @RequestBody BookMarkRequest request) {

        System.out.println("Update bookmark!");

        System.out.printf("Chapter id = %d and paragraph - %d%n", request.getChapterId(), request.getParagraph());
        BookCatalog catalog = bookCatalogService.readById(catalogId);

//        AuthUser authUser = (AuthUser) authentication.getPrincipal();
//
//        Person person = authUser.getPerson();
//
//        if (catalog.getOwner().getId() != person.getId()) {
//            throw new AccessDeniedException("Ви не має доступу до цього ресурсу");
//        }

        BookMark bookMark = bookMarkService.readById(markId);

//        Chapter chapter = chapterService.readById(request.getChapterId());

        int paragraph = request.getParagraph();

        bookMark.setChapterId(request.getChapterId());
        // якщо хочемо зробити закладку на рівні глави
        if (bookMark.getParagraph() == paragraph) {
            System.out.println("Deleting bookMark");

            bookMark.setParagraph(0);

        }

        // якщо хочемо зробити закладку на рівні параграфу глави
        else {
            System.out.println("Updating bookMark");

            bookMark.setParagraph(paragraph);
        }

        return ResponseEntity.ok(bookMarkMapper.dtoToResponse(bookMarkService.update(bookMark)));
    }


    @DeleteMapping("/{markId}")
    public ResponseEntity<?> deleteMark(
            @PathVariable long catalogId,
            @PathVariable long markId) {
        System.out.println("DELETE METHOD BOOKMARK");

//        AuthUser authUser = (AuthUser) authentication.getPrincipal();
//
//        Person person = authUser.getPerson();
//
        bookCatalogService.readById(catalogId);

//        if (catalog.getOwner().getId() != person.getId()) {
//            throw new AccessDeniedException("Ви не має доступу до цього ресурсу");
//        }

//        BookMark mark = bookMarkService.readById(markId);

//        if (mark.getCatalog().getId() != catalog.getId()) {
//
//        }

        bookMarkService.deleteById(markId);

//        return ResponseBookMark.deleteSuccess();
        return ResponseEntity.ok().build();
    }
}
