package com.shadoww.bookcatalogservice.repository;

import com.shadoww.bookcatalogservice.model.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
}
