package com.shadoww.userprofileservice.repository;

import com.shadoww.userprofileservice.model.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    Optional<BookMark> findByBookIdAndOwnerId(long bookId, long ownerId);

    Optional<BookMark> findBookMarkByCatalog_IdAndBookId(long catalogId, long bookId);


//    boolean existsBookMarkByChapterIdAndParagraph(long chapterId, int paragraph);

    Optional<BookMark> findBookMarkByChapterIdAndParagraph(long chapterId, int paragraph);

    void deleteBookMarkByChapterIdAndParagraph(long chapterId, int paragraph);
}
