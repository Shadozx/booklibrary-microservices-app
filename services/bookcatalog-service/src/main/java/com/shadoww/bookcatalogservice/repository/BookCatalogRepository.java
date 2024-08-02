package com.shadoww.bookcatalogservice.repository;

import com.shadoww.bookcatalogservice.model.BookCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookCatalogRepository extends JpaRepository<BookCatalog, Long> {

    List<BookCatalog> findBookCatalogByOwnerId(Long ownerId);

    Optional<BookCatalog> findBookCatalogByIdAndOwnerId(long id, Long ownerId);
}