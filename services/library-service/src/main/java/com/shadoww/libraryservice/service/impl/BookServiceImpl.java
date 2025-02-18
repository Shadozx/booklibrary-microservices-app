package com.shadoww.libraryservice.service.impl;

//import com.shadoww.bookLibrary.dto.request.book.BookFilterRequest;

import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.exception.NotFoundException;
import com.shadoww.api.exception.NullEntityReferenceException;
import com.shadoww.api.exception.ValueAlreadyExistsException;
import com.shadoww.libraryservice.model.Author;
import com.shadoww.libraryservice.model.Book;
import com.shadoww.libraryservice.repository.BookRepository;
import com.shadoww.libraryservice.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public List<Book> getBySearchTitle(String title) {
        // .filter(book -> filterRequest.getSearchText() == null || book.getTitle().toLowerCase().contains(filterRequest.getSearchText()))

        return bookRepository.findByTitleContainsIgnoreCase(title);
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

        Book oldBook = readById(updatedBook.getId());

        if (!Objects.equals(oldBook.getTitle(), updatedBook.getTitle()) && existByTitle(updatedBook.getTitle())) {
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
        List<Book> books = new ArrayList<>();

        if (filterRequest.getSearchText() != null && !filterRequest.getSearchText().equals("")) {
            books.addAll(getBySearchTitle(filterRequest.getSearchText()));
        } else {
            books.addAll(getAll());
        }

        return books.stream()
                .filter(book -> filterRequest.getFromAmount() == null || book.getChapters().size() >= filterRequest.getFromAmount())
                .filter(book -> filterRequest.getToAmount() == null || book.getChapters().size() <= filterRequest.getToAmount())
                .filter(book -> filterRequest.getFromYear() == null || book.getCreatedAt().getYear() >= filterRequest.getFromYear())
                .filter(book -> filterRequest.getToYear() == null || book.getCreatedAt().getYear() <= filterRequest.getToYear())
                .toList();
    }

    @Override
    public List<Book> getAuthorBooks(Author author) {
        return bookRepository.findByAuthors(author);
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
