package com.shadoww.libraryservice.repository;

import com.shadoww.libraryservice.model.BookSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookSeriesRepository extends JpaRepository<BookSeries, Long> {

    boolean existsByTitleIgnoreCase(String title);
    Optional<BookSeries> findByTitleIgnoreCase(String title);
}