package com.shadoww.bookservice.service.impl;

//import com.shadoww.bookLibrary.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.exception.NotFoundException;
import com.shadoww.api.exception.NullEntityReferenceException;
import com.shadoww.api.exception.ValueAlreadyExistsException;
import com.shadoww.bookservice.model.Book;
import com.shadoww.bookservice.repository.BookRepository;
import com.shadoww.bookservice.service.interfaces.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public Book create(Book book) {

        checkIsBookNull(book);

        if (existByTitle(book.getTitle())) {
            throw new ValueAlreadyExistsException("Книга з такою назвою вже існує");
        }

        return save(book);
    }

    @Override
    public Book readById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такої книги не існує"));
    }

//    @Override
//    public Book getByUrl(String url) {
//        return bookRepository.findByUploadedUrl(url).orElseThrow(() -> new EntityNotFoundException("Книжки з такою силкою не існує"));
//    }

    @Override
    public Book getByTitle(String title) {
        return bookRepository
                .findByTitle(title)
                .orElseThrow(() -> new NotFoundException("Книжки з такою назвою не існує"));
    }

    @Override
    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

    @Override
    public boolean existByTitle(String title) {
        return bookRepository.existsBookByTitle(title);
    }

//    @Override
//    public boolean existsByUrl(String uploadedUrl) {
//        return bookRepository.existsBookByUploadedUrl(uploadedUrl);
//    }


    @Override
    @Transactional
    public Book update(Book updatedBook) {

        checkIsBookNull(updatedBook);

        readById(updatedBook.getId());

        if (existByTitle(updatedBook.getTitle())) {
            throw new ValueAlreadyExistsException("Книга з такою назвою вже існує");
        }

        return save(updatedBook);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Book book = readById(id);

        delete(book);
    }

    @Override
    public long count() {
        return bookRepository.count();
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> filterBooks(BookFilterRequest filterRequest) {
        return getAll().stream()
                .filter(book -> filterRequest.getSearchText() == null || book.getTitle().toLowerCase().contains(filterRequest.getSearchText()))
                .filter(book -> filterRequest.getFromAmount() == null || book.getChapters().size() >= filterRequest.getFromAmount())
                .filter(book -> filterRequest.getToAmount() == null || book.getChapters().size() <= filterRequest.getToAmount())
                .filter(book -> filterRequest.getFromYear() == null || book.getCreatedAt().getYear() >= filterRequest.getFromYear())
                .filter(book -> filterRequest.getToYear() == null || book.getCreatedAt().getYear() <= filterRequest.getToYear())
                .toList();
    }

    @Transactional
    Book save(Book book) {
        checkIsBookNull(book);
        return bookRepository.save(book);
    }

    @Transactional
    void delete(Book book) {
        checkIsBookNull(book);
        bookRepository.delete(book);
    }

    private void checkIsBookNull(Book book) {
        if (book == null) {
            throw new NullEntityReferenceException("Книжка не може бути пустою");
        }
    }
}
