package com.shadoww.ratingservice.repository;

import com.shadoww.ratingservice.model.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Long> {

    List<BookRating> findByBookId(long bookId);

    Optional<BookRating> findByOwnerIdAndBookId(Long ownerId, Long bookId);


}
