package com.shadoww.libraryservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
//@Table(name = "chapters")
@Getter
@Setter
@NoArgsConstructor
public class Chapter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Title of chapter cannot be empty")
    @Column(nullable = false, columnDefinition = "varchar")
    private String title;


    @NotBlank(message = "Text of chapter cannot be empty")
    @Column(columnDefinition = "varchar")
    private String text;


    //    @Column(name = "number_of_page")
    @Min(value = 0, message = "Chapter number must be bigger than 0")
    private int chapterNumber;

    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book book;

    public Chapter(long id, String title, int numberOfPage) {
        this.id = id;
        this.title = title;
        this.chapterNumber = numberOfPage;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return Objects.equals(title, chapter.title) && Objects.equals(text, chapter.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text);
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='\n" + text.length() + "\n'" +
                ", number='" + chapterNumber + "'" +
                '}';
    }
}
