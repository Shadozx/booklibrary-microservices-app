package com.shadoww.userprofileservice.service.impl;

import com.shadoww.userprofileservice.model.BookCatalog;
import com.shadoww.userprofileservice.repository.BookCatalogRepository;
import com.shadoww.userprofileservice.service.interfac.BookCatalogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookCatalogServiceImpl implements BookCatalogService {

    private final BookCatalogRepository bookCatalogsRepository;

    @Autowired
    public BookCatalogServiceImpl(BookCatalogRepository bookCatalogsRepository) {
        this.bookCatalogsRepository = bookCatalogsRepository;
    }

    @Override
    @Transactional
    public BookCatalog create(BookCatalog bookCatalog) {

        checkIsCatalogNull(bookCatalog);

        return save(bookCatalog);
    }

    @Override
    public BookCatalog readById(Long id) {
        return bookCatalogsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Такого каталога не існує"));
    }

    @Override
    public boolean existsById(Long id) {
        return bookCatalogsRepository.existsById(id);
    }

    @Override
    public BookCatalog getByIdAndPerson(Long id, Long ownerId) {
        return bookCatalogsRepository
                .findBookCatalogByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Такого каталога не існує"));
    }

    @Override
    public List<BookCatalog> getByPerson(Long ownerId) {
        return bookCatalogsRepository.findBookCatalogByOwnerId(ownerId);
    }

    @Override
    @Transactional
    public BookCatalog update(BookCatalog updatedCatalog) {

        checkIsCatalogNull(updatedCatalog);

        readById(updatedCatalog.getId());

        return save(updatedCatalog);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        delete(readById(id));
    }

    @Override
    public long count() {
        return bookCatalogsRepository.count();
    }

    @Override
    public List<BookCatalog> getAll() {
        return bookCatalogsRepository.findAll();
    }

    @Transactional
    BookCatalog save(BookCatalog catalog) {

        checkIsCatalogNull(catalog);

        if (catalog.getTitle() == null) {
            throw new IllegalArgumentException("Каталог повинен містити назву");
        }

        if (catalog.getOwnerId() == null) {
            throw new IllegalArgumentException("Каталог повинен мати власника");
        }

        return bookCatalogsRepository.save(catalog);
    }
    @Transactional
    void delete(BookCatalog bookCatalog) {

        checkIsCatalogNull(bookCatalog);

        bookCatalogsRepository.delete(bookCatalog);
    }

    private void checkIsCatalogNull(BookCatalog catalog) {
        if (catalog == null) {
            throw new NullPointerException("Каталог не може бути пустим");
        }
    }
}
