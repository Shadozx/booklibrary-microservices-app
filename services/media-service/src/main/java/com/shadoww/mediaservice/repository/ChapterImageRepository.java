package com.shadoww.mediaservice.repository;

import com.shadoww.mediaservice.model.ChapterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterImageRepository extends JpaRepository<ChapterImage, Long> {

    List<ChapterImage> findByBookId(Long bookId);
    List<ChapterImage> findByChapterId(long chapterId);

    void deleteByBookId(Long bookId);
}