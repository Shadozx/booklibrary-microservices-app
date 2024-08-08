package com.shadoww.commentservice.repository;


import com.shadoww.commentservice.model.BookComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
    List<BookComment> findByBookId(Long bookId);

    List<BookComment> findByBookIdAndOwnerId(Long bookId, Long ownerId);

}
