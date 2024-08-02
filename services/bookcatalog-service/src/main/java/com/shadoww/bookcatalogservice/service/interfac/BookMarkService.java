package com.shadoww.bookcatalogservice.service.interfac;

//import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.bookcatalogservice.model.BookMark;
//import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.api.service.CrudService;

public interface BookMarkService extends CrudService<BookMark, Long> {

    BookMark getBookMark(Long chapter, int paragraph);

    BookMark getBookMarkByCatalogAndBook(Long catalogId, Long bookId);

    BookMark getByBookAndOwner(Long bookId, Long ownerId) ;

    void deleteBookMark(Long chapter, int paragraph);
}
