package com.shadoww.commentservice.service.impl;


import com.shadoww.api.exception.NotFoundException;
import com.shadoww.api.exception.NullEntityReferenceException;
import com.shadoww.commentservice.model.*;
import com.shadoww.commentservice.repository.BookCommentRepository;
import com.shadoww.commentservice.repository.ChapterCommentRepository;
import com.shadoww.commentservice.repository.CommentRepository;
import com.shadoww.commentservice.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookCommentRepository bookCommentRepository;

    private final ChapterCommentRepository chapterCommentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              BookCommentRepository bookCommentRepository,
                              ChapterCommentRepository chapterCommentRepository) {
        this.commentRepository = commentRepository;
        this.bookCommentRepository = bookCommentRepository;
        this.chapterCommentRepository = chapterCommentRepository;
    }

    @Override
    @Transactional
    public Comment create(Comment comment) {

        checkIsCommentNull(comment);

        return save(comment);
    }

    @Override
    public Comment readById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Коментарія не було знайдено"));
    }

    @Override
    @Transactional
    public Comment update(Comment comment) {

        checkIsCommentNull(comment);

        readById(comment.getId());
        return save(comment);
    }

    @Override
    public boolean existsById(Long id) {
        return commentRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        delete(readById(id));
    }

    @Override
    public long count() {
        return commentRepository.count();
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<BookComment> getAllBookComments() {
        return bookCommentRepository.findAll();
    }

    @Override
    public List<ChapterComment> getAllChapterComments() {
        return chapterCommentRepository.findAll();
    }

    @Override
    public List<BookComment> getBookComments(long bookId) {
        return bookCommentRepository.findByBookId(bookId);
    }

    @Override
    public List<ChapterComment> getChapterComments(long chapterId) {
        return chapterCommentRepository.findByChapterId(chapterId);
    }

    @Override
    public List<BookComment> getOwnerBookComments(long bookId, long ownerId) {
        return bookCommentRepository.findByBookIdAndOwnerId(bookId, ownerId);
    }

    @Override
    public List<ChapterComment> getOwnerChapterComments(long chapterId, long ownerId) {
        return chapterCommentRepository.findByChapterIdAndOwnerId(chapterId, ownerId);
    }

    @Transactional
    Comment save(Comment comment) {

        checkIsCommentNull(comment);

        return commentRepository.save(comment);
    }

    @Transactional
    void delete(Comment comment) {

        checkIsCommentNull(comment);

        commentRepository.delete(comment);
    }

    private void checkIsCommentNull(Comment comment) {
        if (comment == null) {
            throw new NullEntityReferenceException("Коментарій не може бути пустим");
        }
    }
}
