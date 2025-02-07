package com.shadoww.feedbackservice.mapper;

import com.shadoww.api.dto.request.comment.CommentRequest;
import com.shadoww.api.dto.response.comment.BookCommentResponse;
import com.shadoww.api.dto.response.comment.ChapterCommentResponse;
import com.shadoww.api.dto.response.comment.CommentResponse;
import com.shadoww.feedbackservice.model.comment.*;


public interface CommentMapper {

    CommentRequest dtoToRequest(Comment comment);

    Comment dtoToModel(CommentRequest request);

    CommentResponse dtoToResponse(Comment comment);

    Comment dtoToModel(CommentResponse response);

    BookCommentResponse dtoToResponse(BookComment comment);

    BookComment dtoToModel(BookCommentResponse response);


    ChapterCommentResponse dtoToResponse(ChapterComment comment);

    ChapterComment dtoToModel(ChapterCommentResponse response);
}
