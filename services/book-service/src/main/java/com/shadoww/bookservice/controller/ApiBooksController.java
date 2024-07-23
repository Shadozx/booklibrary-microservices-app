package com.shadoww.bookservice.controller;


import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.exception.NotFoundException;
import com.shadoww.bookservice.mapper.BookMapper;
import com.shadoww.bookservice.model.Book;
import com.shadoww.bookservice.service.interfaces.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.shadoww.api.dto.response.BookResponse;

//@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/books")
public class ApiBooksController {
    private final BookService bookService;

    private final BookMapper bookMapper;

    @Autowired
    public ApiBooksController(
            BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping("/no")
    public ResponseEntity<?> noBook() {
        throw new NotFoundException("test error");
    }

    /**
     * Get books
     */
    @GetMapping
    public ResponseEntity<?> getBooks() {

        return ResponseEntity.ok(
                bookService.getAll()
                        .stream().map(this::mapToBookResponse)
                        .toList()
        );
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterBooks(@RequestBody BookFilterRequest request) {

        return ResponseEntity.ok(bookService.filterBooks(request)
                .stream()
                .map(this::mapToBookResponse)
                .toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable long id) {

        Book book = bookService.readById(id);

        return ResponseEntity.ok(mapToBookResponse(book));

    }

    /**
     * Create a book by book's form
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookRequest form) {

//        if (form.isEmpty()) return ResponseBook.noContent();
//
//        if (form.isTitleEmpty()) return ResponseBook.noContent("Назва книжки немає бути пустою");

//        if (bookService.existByTitle(form.getTitle().trim())) return ResponseBook.exist();


        Book book = new Book();
        book.setTitle(form.getTitle());

        if (!form.isDescriptionEmpty()) {
            book.setDescription(form.getDescription());
        }

//        if (!form.isBookImageUrlEmpty()) {
//            try {
//                BookImage bookImage = (BookImage) ParserHelper.parseImage(form.getBookImage());
//
//                book.setBookImage(bookImage);
//                bookImage.setBook(book);
//
////                bookService.saveBookImage(book, bookImage);
//            } catch (IOException e) {
//                System.out.println("Error message in adding book with message:" + e.getMessage());
//                return ResponseBook.errorServer();
//            }
//        } else {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookMapper.dtoToResponse(bookService.create(book)));
//        }

//        return ResponseBook.addSuccess();

//        return ResponseEntity.ok().build();
    }

    /**
     * Update a book by book's id
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable long bookId,
                                        @RequestBody BookRequest request) {

//        if (request.isEmpty()) return ResponseBook.noContent();

        Book book = bookService.readById(bookId);


        if (!request.isTitleEmpty()) {
            book.setTitle(request.getTitle());
        }

        if (!request.isDescriptionEmpty()) {
            book.setDescription(request.getDescription());
        }

//        if (!request.isBookImageUrlEmpty()) {
//
//            try {
//
//                BookImage parsedImage = (BookImage) ParserHelper.parseImage(request.getBookImage());
//
//                BookImage bookImage = book.getBookImage();
//
//                if (bookImage == null) bookImage = new BookImage();
//
//                bookImage.setData(parsedImage.getData());
//
//                System.out.println("BookImage parsing...");
//                System.out.println(bookImage);
//                book.setBookImage(bookImage);
//                bookImage.setBook(book);
//
////                bookService.saveBookImage(book, bookImage);
//            } catch (IOException e) {
//                System.out.println("Error message in adding book with message:" + e.getMessage());
//                return ResponseBook.errorServer();
//            }
//        }
//        else {
//            bookService.update(book);
//        }

//        return ResponseBook.addSuccess();
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a book by book's id
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable long id) {

        bookService.deleteById(id);

//        return ResponseBook.deleteSuccess();
        return ResponseEntity.ok().build();
    }

    private BookResponse mapToBookResponse(Book book) {
//        return new BookResponse(
//                book.getId(),
//                book.getTitle(),
//                book.getDescription(),
//                book.getAmount(),
//                null,
//                "");

        return bookMapper.dtoToResponse(book);
    }
}
