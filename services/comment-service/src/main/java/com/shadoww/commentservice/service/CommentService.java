package com.shadoww.commentservice.service;

import com.shadoww.commentservice.model.BookComment;
import com.shadoww.commentservice.model.ChapterComment;
import com.shadoww.commentservice.model.Comment;
import com.shadoww.api.service.CrudService;

import java.util.List;


public interface CommentService extends CrudService<Comment, Long> {

    List<BookComment> getAllBookComments();

    List<ChapterComment> getAllChapterComments();

    List<BookComment> getBookComments(long bookId);

    List<ChapterComment> getChapterComments(long chapterId);

    List<BookComment> getOwnerBookComments(long bookId, long ownerId);

    List<ChapterComment> getOwnerChapterComments(long chapterId, long ownerId);
}