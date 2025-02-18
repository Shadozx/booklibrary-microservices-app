package com.shadoww.libraryservice.repository;

import com.shadoww.libraryservice.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameContains(String name);
//    boolean existsByUploadedUrl(String uploadedUrl);
    Optional<Author> findByName(String name);
//    List<Author> findByName(String name);

    boolean existsByName(String name);

}
