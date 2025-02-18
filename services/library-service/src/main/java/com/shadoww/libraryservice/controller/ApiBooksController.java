package com.shadoww.libraryservice.controller;


import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.libraryservice.mapper.AuthorMapper;
import com.shadoww.libraryservice.mapper.BookMapper;
import com.shadoww.libraryservice.model.Author;
import com.shadoww.libraryservice.model.Book;
import com.shadoww.libraryservice.service.interfaces.AuthorService;
import com.shadoww.libraryservice.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/books")
public class ApiBooksController {
    private final BookService bookService;

    private final AuthorService authorService;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    @Autowired
    public ApiBooksController(
            BookService bookService, AuthorService authorService, BookMapper bookMapper, AuthorMapper authorMapper) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
    }


    @PreAuthorize("true")
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Test message");
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
        book.setBookImageId(form.getImageId());

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
        book.setBookImageId(request.getImageId());

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

    @GetMapping("/{bookId}/authors")
    public ResponseEntity<?> getBookAuthors(@PathVariable long bookId) {
        List<AuthorResponse> authors = authorService.getBookAuthors(bookId)
                .stream()
                .map(authorMapper::dtoToResponse)
                .toList();

        return ResponseEntity.ok(authors);
    }

    // add book author
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/authors")
    public ResponseEntity<Void> addAuthor(@PathVariable long id,
                                          @RequestBody Map<String, Long> requestBody) {

        long authorId = requestBody.get("author_id");

        Book book = bookService.readById(id);

        Author author = authorService.readById(authorId);

        List<Author> authors = book.getAuthors();

        if (authors.contains(author)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        authors.add(author);

        bookService.update(book);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // add book author
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/authors")
    public ResponseEntity<Void> removeAuthor(@PathVariable long id,
                                             @RequestBody Map<String, Long> requestBody) {

        long authorId = requestBody.get("author_id");

        Book book = bookService.readById(id);

        Author author = authorService.readById(authorId);

        List<Author> authors = book.getAuthors();

        if (!authors.contains(author)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        authors.remove(author);

        bookService.update(book);

        return ResponseEntity
                .ok()
                .build();
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
