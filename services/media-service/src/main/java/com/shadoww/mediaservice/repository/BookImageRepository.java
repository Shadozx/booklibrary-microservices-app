package com.shadoww.mediaservice.repository;

import com.shadoww.mediaservice.model.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {

    Optional<BookImage> findBookImageByBookId(long bookId);

    void deleteByBookId(Long bookId);
}