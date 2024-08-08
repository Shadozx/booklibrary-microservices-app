package com.shadoww.commentservice.repository;


import com.shadoww.commentservice.model.ChapterComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterCommentRepository extends JpaRepository<ChapterComment, Long> {

    List<ChapterComment> findByChapterId(Long chapterId);

    List<ChapterComment> findByChapterIdAndOwnerId(Long chapterId, Long ownerId);
}
