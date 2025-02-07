package com.shadoww.libraryservice.service.impl;


import com.shadoww.api.exception.NotFoundException;
import com.shadoww.api.exception.NullEntityReferenceException;
import com.shadoww.libraryservice.model.Book;
import com.shadoww.libraryservice.model.Chapter;
import com.shadoww.libraryservice.repository.ChapterRepository;
import com.shadoww.libraryservice.service.interfaces.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterServiceImpl(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @Override
    @Transactional
    public Chapter create(Chapter chapter) {

        checkIsChapterNull(chapter);

        return save(chapter);
    }

    @Override
    public Chapter readById(Long id) {
        return chapterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такого розділу не існує"));
    }

    @Override
    public Chapter getChapterByBookAndNumber(Book book, int number) {
        return chapterRepository.findChapterByBookAndChapterNumber(book, number)
                .orElseThrow(() -> new NotFoundException("Такого розділу не існує"));
    }

    @Override
    public List<Chapter> getBookChapters(Book book) {
        List<Chapter> chapters = chapterRepository.findAllByBookId(book, Sort.by(Sort.Direction.ASC, "chapterNumber"));
        Collections.reverse(chapters);
        return chapters;
    }

    @Override
    @Transactional
    public Chapter update(Chapter updated) {

        checkIsChapterNull(updated);

        readById(updated.getId());

        return save(updated);
    }

    @Override
    public boolean existsById(Long id) {
        return chapterRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        delete(readById(id));
    }

    @Override
    public long count() {
        return chapterRepository.count();
    }

    @Override
    public List<Chapter> getAll() {
        return chapterRepository.findAll();
    }

    @Transactional
    Chapter save(Chapter c) {

        checkIsChapterNull(c);
        return chapterRepository.save(c);
    }

    @Transactional
    void delete(Chapter chapter) {

        checkIsChapterNull(chapter);
        chapterRepository.delete(chapter);
    }


    @Transactional
    public void deleteByBook(long id) {
        chapterRepository.deleteChaptersByBook_Id(id);
    }

    private void checkIsChapterNull(Chapter chapter) {
        if (chapter == null) {
            throw new NullEntityReferenceException("Розділ книги не може бути пустим");
        }
    }
}
