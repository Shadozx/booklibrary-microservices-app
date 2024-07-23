package com.shadoww.bookservice.repository;

import com.shadoww.bookservice.model.Book;
import com.shadoww.bookservice.model.Chapter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findChaptersByBook(Book book, Sort sort);

    @Query("SELECT new Chapter(c.id, c.title, c.chapterNumber) FROM Chapter c WHERE c.book = :book")
    List<Chapter> findAllByBookId(@Param("book") Book book, Sort sort);

    List<Chapter> findChaptersByBookId(int bookId, Sort sort);

    Optional<Chapter> findChaptersByBookAndChapterNumber(Book book, int numberOfPage);

    Optional<Chapter> findChapterByBookAndChapterNumber(Book book, int numberOfPage);

    void deleteChaptersByBook_Id(long bookId);
}
