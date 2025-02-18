package com.shadoww.libraryservice.repository;


import com.shadoww.libraryservice.model.Author;
import com.shadoww.libraryservice.model.Book;
import com.shadoww.libraryservice.model.BookSeries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBySeries(List<BookSeries> series);
    List<Book> findByTitleContainsIgnoreCase(String title);
//    List<Book> findByTitleAllIgnoreCaseOrderByChapters_TextAsc(String title);

    Optional<Book> findByTitle(String title);

//    List<Book> findTop10ByOrderByAddedDesc();

    Page<Book> findByTitleContainingIgnoreCase(Pageable page, String title);

//    Page<Book> findBooksByOrderByCreatedAtDesc(Pageable page);

    boolean existsBookByTitle(String title);

    List<Book> findByAuthors(List<Author> authors);
//    List<Book> findFirst1OrderByAdded();
}