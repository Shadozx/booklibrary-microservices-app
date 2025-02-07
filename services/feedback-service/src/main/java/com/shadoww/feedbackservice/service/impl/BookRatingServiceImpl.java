package com.shadoww.feedbackservice.service.impl;

import com.shadoww.api.exception.NotFoundException;
import com.shadoww.api.exception.NullEntityReferenceException;
import com.shadoww.feedbackservice.model.rating.BookRating;
import com.shadoww.feedbackservice.repository.BookRatingRepository;
import com.shadoww.feedbackservice.service.interfaces.BookRatingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookRatingServiceImpl implements BookRatingService {

    private final BookRatingRepository bookRatingsRepository;

    @Autowired
    public BookRatingServiceImpl(BookRatingRepository bookRatingsRepository) {
        this.bookRatingsRepository = bookRatingsRepository;
    }

    @Override
    @Transactional
    public BookRating create(BookRating rating) {
        checkIsRatingNull(rating);

        return save(rating);
    }

    @Override
    public BookRating readById(Long ratingId) {
        return bookRatingsRepository.findById(ratingId).orElseThrow(()->new EntityNotFoundException("Такої оцінки не існує"));
    }

    @Override
    public List<BookRating> getBookRatings(long bookId) {
        return bookRatingsRepository.findByBookId(bookId);
    }

    @Override
    public BookRating getOwnerBookRating(long ownerId, long bookId) {
        return bookRatingsRepository.findByOwnerIdAndBookId(ownerId, bookId)
                .orElseThrow(()->new NotFoundException("Такої оцінки не існує"));
    }

    @Override
    @Transactional
    public BookRating update(BookRating rating) {
        checkIsRatingNull(rating);

        BookRating old = readById(rating.getId());

        old.setRating(rating.getRating());

        return save(old);
    }

    @Override
    public boolean existsById(Long id) {
        return bookRatingsRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        delete(readById(id));
    }

    @Override
    public long count() {
        return bookRatingsRepository.count();
    }

    @Override
    public List<BookRating> getAll() {
        return bookRatingsRepository.findAll();
    }

    @Transactional
    BookRating save(BookRating rating) {
        checkIsRatingNull(rating);

        return bookRatingsRepository.save(rating);
    }

    @Transactional
    void delete(BookRating rating) {
        checkIsRatingNull(rating);

        bookRatingsRepository.delete(rating);
    }

    private void checkIsRatingNull(BookRating rating) {
        if(rating == null) {
            throw new NullEntityReferenceException("Оцінка книги не може бути пустою");
        }
    }
}
