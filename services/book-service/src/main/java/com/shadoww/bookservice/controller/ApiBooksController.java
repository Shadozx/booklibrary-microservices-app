package com.shadoww.bookservice.controller;


import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.bookservice.mapper.BookMapper;
import com.shadoww.bookservice.model.Book;
import com.shadoww.bookservice.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/books")
public class ApiBooksController {
    private final BookService bookService;

    private final BookMapper bookMapper;

    @Autowired
    public ApiBooksController(
            BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    /**
     * Get books
     */
    @GetMapping
    public ResponseEntity<?> getBooks() {

        return ResponseEntity.ok(
                bookService.getAll()
                        .stream().map(bookMapper::dtoToResponse)
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBook(
            @RequestBody BookRequest form) {

        if (form.isTitleEmpty()) {
            return noContent("Назва книжки немає бути пустою");
        }

        Book book = new Book();
        book.setTitle(form.getTitle());

        book.setDescription(form.getDescription());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookMapper.dtoToResponse(bookService.create(book)));
    }

    /**
     * Update a book by book's id
     */
//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable long bookId,
                                        @RequestBody BookRequest request) {

        Book book = bookService.readById(bookId);

        if (!request.isTitleEmpty()) {
            book.setTitle(request.getTitle());
        }

        book.setDescription(request.getDescription());

//        if (!request.isDescriptionEmpty()) {
//            book.setDescription(request.getDescription());
//        }

        return ResponseEntity.ok(
                        bookMapper.dtoToResponse(bookService.update(book))
                );
    }

    /**
     * Delete a book by book's id
     */

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable long id) {

        bookService.deleteById(id);

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

    private ResponseEntity<?> noContent(String message) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(message);
    }
}
