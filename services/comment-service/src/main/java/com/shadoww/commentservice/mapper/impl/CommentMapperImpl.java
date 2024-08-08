package com.shadoww.commentservice.mapper.impl;

import com.shadoww.api.dto.request.comment.CommentRequest;
import com.shadoww.api.dto.response.comment.BookCommentResponse;
import com.shadoww.api.dto.response.comment.ChapterCommentResponse;
import com.shadoww.api.dto.response.comment.CommentResponse;
import com.shadoww.commentservice.mapper.CommentMapper;
import com.shadoww.commentservice.model.BookComment;
import com.shadoww.commentservice.model.ChapterComment;
import com.shadoww.commentservice.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public CommentRequest dtoToRequest(Comment comment) {

        CommentRequest request = new CommentRequest();

        request.setText(comment.getText());

        return request;
    }

    @Override
    public Comment dtoToModel(CommentRequest request) {
        Comment comment = new Comment();

        comment.setId(null);
        comment.setText(request.getText());

        return comment;
    }

    @Override
    public CommentResponse dtoToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getOwnerId()
        );
    }

    @Override
    public Comment dtoToModel(CommentResponse response) {
        Comment comment = new Comment();

        comment.setId(response.getId());
        comment.setText(response.getText());
        comment.setCreatedAt(response.getCreatedAt());
        comment.setOwnerId(response.getOwnerId());

        return comment;
    }

    @Override
    public BookCommentResponse dtoToResponse(BookComment comment) {
        return new BookCommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getOwnerId(),
                comment.getBookId()
        );
    }

    @Override
    public BookComment dtoToModel(BookCommentResponse response) {
        BookComment comment = new BookComment();

        comment.setId(response.getId());
        comment.setText(response.getText());
        comment.setCreatedAt(response.getCreatedAt());
        comment.setOwnerId(response.getOwnerId());
        comment.setBookId(response.getBookId());

        return comment;
    }

    @Override
    public ChapterCommentResponse dtoToResponse(ChapterComment comment) {
        return new ChapterCommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getOwnerId(),
                comment.getChapterId()
        );
    }

    @Override
    public ChapterComment dtoToModel(ChapterCommentResponse response) {
        ChapterComment comment = new ChapterComment();

        comment.setId(response.getId());
        comment.setText(response.getText());
        comment.setCreatedAt(response.getCreatedAt());
        comment.setOwnerId(response.getOwnerId());
        comment.setChapterId(response.getChapterId());

        return comment;
    }
}