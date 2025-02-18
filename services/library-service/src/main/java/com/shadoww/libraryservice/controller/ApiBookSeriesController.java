package com.shadoww.libraryservice.controller;

import com.shadoww.api.dto.request.BookSeriesRequest;
import com.shadoww.api.exception.ValueAlreadyExistsException;
import com.shadoww.libraryservice.mapper.BookMapper;
import com.shadoww.libraryservice.mapper.BookSeriesMapper;
import com.shadoww.libraryservice.model.BookSeries;
import com.shadoww.libraryservice.service.interfaces.BookSeriesService;
import com.shadoww.libraryservice.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/bookseries")
public class ApiBookSeriesController {

    private final BookService bookService;
    private final BookSeriesService bookSeriesService;

    private final BookSeriesMapper bookSeriesMapper;
    private final BookMapper bookMapper;
    @Autowired
    public ApiBookSeriesController(BookService bookService, BookSeriesService bookSeriesService, BookSeriesMapper bookSeriesMapper, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookSeriesService = bookSeriesService;
        this.bookSeriesMapper = bookSeriesMapper;
        this.bookMapper = bookMapper;
    }


    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(bookSeriesService.getAll()
                .stream()
                .map(bookSeriesMapper::dtoToResponse)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookSeries(@PathVariable long id) {
        return ResponseEntity.ok(bookSeriesMapper.dtoToResponse(bookSeriesService.readById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookSeries(@PathVariable long id, @RequestBody BookSeriesRequest bookSeriesRequest) {

        if (bookSeriesService.existsByTitle(bookSeriesRequest.getTitle())) {
            throw new ValueAlreadyExistsException("Серія книг з такою назвою вже існує!");
        }

        BookSeries bookSeries = bookSeriesService.readById(id);

        if (bookSeriesRequest.getTitle() != null) {
            bookSeries.setTitle(bookSeriesRequest.getTitle());
        }

        if (bookSeriesRequest.getDescription() != null) {
            bookSeries.setDescription(bookSeries.getDescription());
        }

        return ResponseEntity.ok(bookSeriesMapper.dtoToResponse(bookSeriesService.update(bookSeries)));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookSeries(@PathVariable long id) {
        bookSeriesService.deleteById(id);

        return ResponseEntity.ok("Серія книг була видалена");
    }

    @GetMapping("/{seriesId}/books")
    public ResponseEntity<?> getBookSeriesBooks(@PathVariable long seriesId) {
        BookSeries bookSeries = bookSeriesService.readById(seriesId);

        return ResponseEntity.ok(
                bookService.getBookSeriesBooks(bookSeries)
                        .stream()
                        .map(bookMapper::dtoToResponse)
                        .toList()
        );
    }
}