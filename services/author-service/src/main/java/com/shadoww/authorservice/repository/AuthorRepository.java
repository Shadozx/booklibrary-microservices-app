package com.shadoww.authorservice.repository;

import com.shadoww.authorservice.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
//    boolean existsByUploadedUrl(String uploadedUrl);
    Optional<Author> findByName(String name);
    boolean existsByName(String name);

}
