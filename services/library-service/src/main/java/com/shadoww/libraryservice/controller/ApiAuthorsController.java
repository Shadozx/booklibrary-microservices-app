package com.shadoww.libraryservice.controller;

import com.shadoww.api.dto.request.AuthorRequest;
import com.shadoww.api.exception.ValueAlreadyExistsException;
import com.shadoww.libraryservice.mapper.AuthorMapper;
import com.shadoww.libraryservice.mapper.BookMapper;
import com.shadoww.libraryservice.model.Author;
import com.shadoww.libraryservice.service.interfaces.AuthorService;
import com.shadoww.libraryservice.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/authors")
public class ApiAuthorsController {

    private final AuthorService authorService;

    private final BookService bookService;

    private final AuthorMapper authorMapper;

    private final BookMapper bookMapper;

    @Autowired
    public ApiAuthorsController(
            AuthorService authorService, BookService bookService, AuthorMapper authorMapper, BookMapper bookMapper) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.authorMapper = authorMapper;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public ResponseEntity<?> getAuthors() {
        return ResponseEntity.ok(authorService.getAll()
                .stream()
                .map(authorMapper::dtoToResponse)
                .toList());
    }

    @PostMapping
    public ResponseEntity<?> createAuthor(@RequestBody AuthorRequest request) {
        Author author = authorMapper.dtoToModel(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorMapper.dtoToResponse(authorService.create(author)));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable long id) {
        return ResponseEntity.ok(
                authorMapper.dtoToResponse(authorService.readById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable long id,
                                          @RequestBody AuthorRequest request) {

        System.out.println(request);

        if (authorService.existsByName(request.getName())) {
            throw new ValueAlreadyExistsException("Автор з таким іменем вже існує!");
        }

        Author author = authorService.readById(id);

        if (request.getName() != null) {
            author.setName(request.getName());
        }

        author.setBiography(request.getBiography());

        return ResponseEntity.ok(authorMapper.dtoToResponse(authorService.update(author)));
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable long id) {
        authorService.deleteById(id);

        return ResponseEntity.ok("Автор був видалений");
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterAuthors(@RequestBody AuthorRequest request) {
        return ResponseEntity.ok(
                authorService.filterAuthors(request)
                        .stream()
                        .map(authorMapper::dtoToResponse)
                        .toList()
        );
    }

    @GetMapping("/{authorId}/books")
    public ResponseEntity<?> getAuthorBooks(@PathVariable long authorId) {
        Author author = authorService.readById(authorId);

        return ResponseEntity.ok(
                bookService.getAuthorBooks(author)
                        .stream()
                        .map(bookMapper::dtoToResponse)
                        .toList()
        );
    }
}