package com.shadoww.userprofileservice.model;

//import com.shadoww.BookLibraryApp.model.user.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private BookCatalog catalog;

    @NotBlank(message = "BookMark must have book")
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Min(value = 0)
    private int paragraph = 0;

    @NotBlank(message = "BookMark must have owner")
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