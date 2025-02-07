package com.shadoww.feedbackservice.controller.comment;

import com.shadoww.api.dto.request.comment.CommentRequest;
import com.shadoww.api.dto.response.comment.BookCommentResponse;
import com.shadoww.api.dto.response.comment.ChapterCommentResponse;
import com.shadoww.feedbackservice.mapper.CommentMapper;
import com.shadoww.feedbackservice.model.comment.*;
import com.shadoww.feedbackservice.service.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/users/{userId}/comments")
public class ApiUserCommentsController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @Autowired
    public ApiUserCommentsController(CommentService commentService,
                                     CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping("/books/{bookId}")
    public ResponseEntity<?> createBookComment(@PathVariable long userId,
                                               @PathVariable long bookId,
                                               @RequestBody CommentRequest request) {
        BookComment comment = new BookComment();
        comment.setText(request.getText());
        comment.setBookId(bookId);
        comment.setOwnerId(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentMapper.dtoToResponse((BookComment) commentService.create(comment)));
    }

    @GetMapping("/books/{bookId}")
    public List<BookCommentResponse> getBookCommentsByBookAndOwner(@PathVariable long userId,
                                                                   @PathVariable long bookId) {
        return commentService.getOwnerBookComments(bookId, userId)
                .stream()
                .map(commentMapper::dtoToResponse)
                .toList();
    }


    @PostMapping("/chapters/{chapterId}")
    public ResponseEntity<?> createChapterComment(@PathVariable long userId,
                                                  @PathVariable long chapterId,
                                                  @RequestBody CommentRequest request) {
        ChapterComment comment = new ChapterComment();
        comment.setText(request.getText());
        comment.setChapterId(chapterId);
        comment.setOwnerId(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentMapper.dtoToResponse((ChapterComment) commentService.create(comment)));
    }

    @GetMapping("/chapters/{chapterId}")
    public List<ChapterCommentResponse> getBookCommentsByChapterAndOwner(@PathVariable long userId,
                                                                         @PathVariable long chapterId) {
        return commentService.getOwnerChapterComments(chapterId, userId)
                .stream()
                .map(commentMapper::dtoToResponse)
                .toList();
    }

//    @PreAuthorize("hasRole('ADMIN') || #userId == principal.id")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable long userId,
                                               @PathVariable long commentId) {

        Comment comment = commentService.readById(commentId);

        if (comment.getOwnerId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentService.deleteById(commentId);

        return ResponseEntity.ok().build();
    }

}
