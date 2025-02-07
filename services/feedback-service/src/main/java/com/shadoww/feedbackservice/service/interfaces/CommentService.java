package com.shadoww.feedbackservice.service.interfaces;

import com.shadoww.feedbackservice.model.comment.*;

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