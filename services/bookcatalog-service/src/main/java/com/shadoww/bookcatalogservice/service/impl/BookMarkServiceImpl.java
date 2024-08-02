package com.shadoww.bookcatalogservice.service.impl;

//import com.shadoww.BookLibraryApp.model.Book;

import com.shadoww.api.exception.NotFoundException;
import com.shadoww.bookcatalogservice.model.BookMark;
//import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.bookcatalogservice.repository.BookMarkRepository;
import com.shadoww.bookcatalogservice.service.interfac.BookMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@Transactional(readOnly = true)
public class BookMarkServiceImpl implements BookMarkService {

    private final BookMarkRepository bookMarkRepository;


    //    private final Supplier<?> defaultNoFoundBookMark =  () -> new NotFoundException("Такої закладки книги не існує");
    @Autowired
    public BookMarkServiceImpl(BookMarkRepository bookMarkRepository) {
        this.bookMarkRepository = bookMarkRepository;
    }

    @Override
    @Transactional
    public BookMark create(BookMark bookMark) {

        checkIsBookMarkNull(bookMark);

        return save(bookMark);
    }

    @Override
    public BookMark readById(Long id) {

        return bookMarkRepository.findById(id)
                .orElseThrow(notFoundException());
    }

    @Override
    public BookMark getBookMark(Long chapter, int paragraph) {

        return bookMarkRepository.findBookMarkByChapterIdAndParagraph(chapter, paragraph)
                .orElseThrow(notFoundException());
    }

    @Override
    public BookMark getBookMarkByCatalogAndBook(Long catalogId, Long bookId) {
        return bookMarkRepository.findBookMarkByCatalog_IdAndBookId(catalogId, bookId)
                .orElseThrow(notFoundException());
    }

    @Override
    public BookMark getByBookAndOwner(Long bookId, Long ownerId) {
        return bookMarkRepository
                .findByBookIdAndOwnerId(bookId, ownerId)
                .orElseThrow(notFoundException());
    }

//    @Override
//    public BookMark getByBookAndOwner(Book book, Person owner) {
//        return bookMarkRepository.findByBookAndOwner(book, owner).orElseThrow(() -> new NotFoundException("Такої закладки не існує"));
//    }

    @Override
    @Transactional
    public BookMark update(BookMark updatedMark) {
        checkIsBookMarkNull(updatedMark);

        readById(updatedMark.getId());

        return save(updatedMark);
    }

    @Override
    public boolean existsById(Long id) {
        return bookMarkRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteBookMark(Long chapter, int paragraph) {
        bookMarkRepository.deleteBookMarkByChapterIdAndParagraph(chapter, paragraph);
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        BookMark mark = readById(id);
        delete(mark);
    }

    @Override
    public long count() {
        return bookMarkRepository.count();
    }

    @Override
    public List<BookMark> getAll() {
        return bookMarkRepository.findAll();
    }

    @Transactional
    BookMark save(BookMark bookMark) {
        checkIsBookMarkNull(bookMark);

        return bookMarkRepository.save(bookMark);
    }

    @Transactional
    void delete(BookMark bookMark) {
        checkIsBookMarkNull(bookMark);

        bookMarkRepository.delete(bookMark);
    }

    private void checkIsBookMarkNull(BookMark mark) {
        if (mark == null) {
            throw new NullPointerException("Закладка не може бути пустою");
        }

        if (mark.getBookId() == null) {
            throw new IllegalArgumentException("Закладка повинна містити книгу");
        }
        if (mark.getOwnerId() == null) {
            throw new IllegalArgumentException("Закладка повинна мати власника");
        }
        if (mark.getCatalog() == null) {
            throw new IllegalArgumentException("Закладка повинна бути в каталозі");
        }

        if (mark.getParagraph() < 0) {
            throw new IllegalArgumentException("Номер параграфа повинен бути більше рівне 0");
        }
    }

    private Supplier<NotFoundException> notFoundException() {
        return notFoundException("Такої закладки книги не існує");
    }

    private Supplier<NotFoundException> notFoundException(String message) {
        return () -> new NotFoundException(message);
    }
}