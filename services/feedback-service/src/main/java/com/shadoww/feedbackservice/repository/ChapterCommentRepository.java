package com.shadoww.feedbackservice.repository;


import com.shadoww.feedbackservice.model.comment.ChapterComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterCommentRepository extends JpaRepository<ChapterComment, Long> {

    List<ChapterComment> findByChapterId(Long chapterId);

    List<ChapterComment> findByChapterIdAndOwnerId(Long chapterId, Long ownerId);
}
