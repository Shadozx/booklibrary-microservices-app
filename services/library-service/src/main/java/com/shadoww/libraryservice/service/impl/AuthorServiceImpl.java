package com.shadoww.libraryservice.service.impl;

import com.shadoww.api.dto.request.AuthorRequest;
import com.shadoww.api.exception.NotFoundException;
import com.shadoww.api.exception.ValueAlreadyExistsException;
import com.shadoww.libraryservice.model.Author;
import com.shadoww.libraryservice.repository.AuthorRepository;
import com.shadoww.libraryservice.service.interfaces.AuthorService;
import com.shadoww.libraryservice.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final BookService bookService;
//    private final Validator validator;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, BookService bookService) {
        this.authorRepository = authorRepository;

//        validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.bookService = bookService;
    }

    @Override
    @Transactional
    public Author create(Author author) {
        checkIsAuthorNull(author);

        if (existsByName(author.getName())) {
            throw new ValueAlreadyExistsException("Автор з таким іменем вже існує!");
        }

        return save(author);
    }

    @Override
    public Author readById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такого автора не існує..."));
    }

    @Override
    public Author readByName(String name) {
        return findByName(name)
                .orElseThrow(() -> new NotFoundException("Такого автора не існує!"));
    }

    @Override
    public boolean existsById(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return authorRepository.existsByName(name);
    }

    @Override
    public List<Author> filterAuthors(AuthorRequest request) {
        return authorRepository.findByNameContains(request.getName());
    }

//    @Override
//    public boolean existsByUrl(String url) {
//        return authorRepository.existsByUploadedUrl(url);
//    }


    @Override
    @Transactional
    public Author update(Author author) {
        checkIsAuthorNull(author);

        readById(author.getId());

        return save(author);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Author author = readById(id);

        delete(author);
    }

    @Override
    public long count() {
        return authorRepository.count();
    }


    Optional<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> getBookAuthors(Long bookId) {
        return bookService.readById(bookId).getAuthors();
    }

    @Transactional
    Author save(Author author) {
        checkIsAuthorNull(author);

        return authorRepository.save(author);
    }

    @Transactional
    void delete(Author author) {
        checkIsAuthorNull(author);

        authorRepository.delete(author);
    }

    private void checkIsAuthorNull(Author author) {
        if (author == null) {
            throw new NotFoundException("Автор не може бути пустим");
        }
    }
}
