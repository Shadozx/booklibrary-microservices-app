package com.shadoww.bookcatalogservice.model;

//import com.shadoww.BookLibraryApp.model.user.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Entity
//@Table(name = "bookmarks")
@Setter
@Getter
@NoArgsConstructor
public class BookMark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Закладка в книжці
     *
     **/
    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private BookCatalog catalog;


    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Min(value = 0)
    private int paragraph = 0;


    @Column(name = "owner_id", nullable = false)
//    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Long ownerId;


    @Override
    public String toString() {
        return "BookMark{" +
                ", paragraph=" + paragraph +
                '}';
    }
}