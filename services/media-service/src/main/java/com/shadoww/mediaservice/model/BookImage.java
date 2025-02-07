package com.shadoww.mediaservice.model;


//import com.shadoww.BookLibraryApp.model.Book;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Table(name = "book_images")
@DiscriminatorValue("Book")
@Setter
@Getter
@NoArgsConstructor
public class BookImage extends Image {

    @Column(name = "book_id")
    private Long bookId;

    public BookImage(Image image) {
        this.setContentType(image.getContentType());
        this.setData(image.getData());
    }

    public void setBook(Long bookId) {
        if (bookId != null) {
            this.bookId = bookId;
            this.setFileName(bookId + ".jpeg");
        }
    }


}

