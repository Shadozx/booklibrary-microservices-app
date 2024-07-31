package com.shadoww.bookcatalogservice.repository;

import com.shadoww.bookcatalogservice.model.BookCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCatalogRepository extends JpaRepository<BookCatalog, Long> {
}
