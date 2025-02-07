package com.shadoww.libraryservice.repository;


import com.shadoww.libraryservice.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainsIgnoreCase(String title);
    List<Book> findByTitleAllIgnoreCaseOrderByChapters_TextAsc(String title);

    Optional<Book> findByTitle(String title);
//    List<Book> findByAuthors(Author authors);
//    List<Book> findBySeries(BookSeries series);

//    Optional<Book> findByUploadedUrl(String uploadedUrl);



//    List<Book> findTop10ByOrderByAddedDesc();

    Page<Book> findByTitleContainingIgnoreCase(Pageable page, String title);

//    Page<Book> findBooksByOrderByCreatedAtDesc(Pageable page);

//    boolean existsBookByUploadedUrl(String uploadedUrl);

    boolean existsBookByTitle(String title);
//    List<Book> findFirst1OrderByAdded();
}