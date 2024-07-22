package com.shadoww.authorservice.service.impl;

import com.shadoww.authorservice.model.Author;
import com.shadoww.authorservice.repository.AuthorRepository;
import com.shadoww.authorservice.service.interfaces.AuthorService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;


    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Author create(Author author) {
        checkIsAuthorNull(author);

        return save(author);
    }

    @Override
    public Author readById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такого автора не існує..."));
    }

    @Override
    public Author readByName(String name) {
        return findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Такого автора не існує!"));
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
    public boolean existsByUrl(String url) {
        return authorRepository.existsByUploadedUrl(url);
    }


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
            throw new NullPointerException("Автор не може бути пустим");
        }
    }
}
