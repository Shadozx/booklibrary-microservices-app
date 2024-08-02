package com.shadoww.api.dto.response;

//import com.shadoww.api.model.BookMark;
//import com.shadoww.BookLibraryApp.model.Chapter;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
//@Setter
//@Getter
public class BookMarkResponse {

    long id;

    int paragraph;

    long catalogId;

    long bookId;
    long chapterId;
    long ownerId;

//    public BookMarkResponse(BookMark mark) {
//        this.id = mark.getId();
//        this.paragraph = mark.getParagraph();
//        this.bookId = mark.getBook().getId();
//        this.catalogId = mark.getCatalog().getId();
//        this.ownerId = mark.getOwner().getId();
//
//        Chapter chapter = mark.getChapter();
//
//        if (chapter != null) {
//            this.chapterId = chapter.getId();
//
//        }else {
//            this.chapterId = 0;
//        }
//    }
}
