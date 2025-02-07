package com.shadoww.userprofileservice.service.interfac;

import com.shadoww.userprofileservice.model.BookCatalog;
import com.shadoww.api.service.CrudService;

import java.util.List;

public interface BookCatalogService extends CrudService<BookCatalog, Long> {

    BookCatalog getByIdAndPerson(Long id, Long ownerId);

    List<BookCatalog> getByPerson(Long ownerId);
}
