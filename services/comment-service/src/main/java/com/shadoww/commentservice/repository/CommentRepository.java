package com.shadoww.commentservice.repository;

import com.shadoww.commentservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
//    List<Comment> findByOwnerId(Long ownerId);

}