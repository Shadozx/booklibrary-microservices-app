package com.shadoww.ratingservice.controller;

import com.shadoww.api.dto.request.BookRatingRequest;
import com.shadoww.ratingservice.mapper.BookRatingMapper;
import com.shadoww.ratingservice.model.BookRating;
import com.shadoww.ratingservice.service.BookRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/ratings")
public class ApiBookRatingsController {

    private final BookRatingService bookRatingService;

    private final BookRatingMapper bookRatingMapper;

    @Autowired
    public ApiBookRatingsController(BookRatingService bookRatingService, BookRatingMapper bookRatingMapper) {
        this.bookRatingService = bookRatingService;
        this.bookRatingMapper = bookRatingMapper;
    }

    @GetMapping
    public ResponseEntity<?> getBookRating(@PathVariable long userId,
                                           @RequestParam long bookId) {

        System.out.println(userId);
        System.out.println(bookId);

        return ResponseEntity.ok(bookRatingMapper.dtoToResponse(bookRatingService.getOwnerBookRating(userId, bookId)));
    }

    @PostMapping
    public ResponseEntity<?> createBookRating(@PathVariable long userId,
                                              @RequestBody BookRatingRequest request) {

        BookRating rating = new BookRating();

        rating.setOwnerId(userId);
        rating.setRating(request.getRating());

        rating.setBookId(request.getBookId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookRatingMapper.dtoToResponse(bookRatingService.create(rating)));
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<?> updateBookRating(@PathVariable long userId,
                                              @PathVariable long ratingId,
                                              @RequestBody BookRatingRequest request) {

        BookRating rating = bookRatingService.readById(ratingId);

        if (rating.getOwnerId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        rating.setRating(request.getRating());

        return ResponseEntity.status(HttpStatus.OK).body(bookRatingMapper.dtoToResponse(bookRatingService.update(rating)));
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteBookRating(@PathVariable long userId,
                                              @PathVariable long ratingId) {


        BookRating rating = bookRatingService.readById(ratingId);

        if (rating.getOwnerId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        bookRatingService.deleteById(ratingId);
        return ResponseEntity.ok().build();
    }
}