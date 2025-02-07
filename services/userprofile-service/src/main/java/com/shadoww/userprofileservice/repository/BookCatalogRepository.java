package com.shadoww.userprofileservice.repository;

import com.shadoww.userprofileservice.model.BookCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookCatalogRepository extends JpaRepository<BookCatalog, Long> {

    List<BookCatalog> findBookCatalogByOwnerId(Long ownerId);

    Optional<BookCatalog> findBookCatalogByIdAndOwnerId(long id, Long ownerId);
}