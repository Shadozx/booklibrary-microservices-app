package com.shadoww.commentservice.controller;


import com.shadoww.api.dto.response.comment.BookCommentResponse;
import com.shadoww.api.dto.response.comment.ChapterCommentResponse;
import com.shadoww.api.dto.response.comment.CommentResponse;
import com.shadoww.commentservice.mapper.CommentMapper;
import com.shadoww.commentservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/comments")
public class ApiCommentsController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;
    @Autowired
    public ApiCommentsController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable long commentId) {
        return ResponseEntity.ok(commentMapper.dtoToResponse(commentService.readById(commentId)));
    }

    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentService.getAll()
                .stream()
                .map(commentMapper::dtoToResponse)
                .toList();
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<List<BookCommentResponse>> getBookCommentsByBookAndOwner(@PathVariable long bookId) {
        return ResponseEntity.ok(commentService.getBookComments(bookId)
                .stream()
                .map(commentMapper::dtoToResponse)
                .toList()
        );
    }

    @GetMapping("/chapters/{chapterId}")
    public ResponseEntity<List<ChapterCommentResponse>> getBookCommentsByChapterAndOwner(@PathVariable long chapterId) {
        return ResponseEntity.ok(commentService.getChapterComments(chapterId)
                .stream()
                .map(commentMapper::dtoToResponse)
                .toList());
    }
}
