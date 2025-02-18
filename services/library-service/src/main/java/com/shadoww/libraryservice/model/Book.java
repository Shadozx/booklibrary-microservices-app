package com.shadoww.libraryservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Назва книжки
     **/
    @NotBlank(message = "Title of book cannot be empty")
    @Column(unique = true, nullable = false)
    private String title;

    /**
     * Опис книжки
     **/
    @Column(columnDefinition = "varchar")
    private String description;

    /**
     * Кількість глав книжки
     **/
    @Min(value = 0, message = "Amount of chapters must be bigger than 0")
    private int amount;

    /**
     * Коли була добавлена книжка
     **/
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

//    @Size(min = 1)
    @Column(name = "image_id")
    private Long bookImageId;

    /**
     * Глави книжки
     **/
    @OneToMany(mappedBy = "book", orphanRemoval = true, fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    private List<Chapter> chapters = new ArrayList<>();

    /**
     * Автори книжки
     **/
    @ManyToMany(
            cascade = {
                    jakarta.persistence.CascadeType.DETACH,
                    jakarta.persistence.CascadeType.MERGE,
                    jakarta.persistence.CascadeType.PERSIST,
                    jakarta.persistence.CascadeType.REFRESH
            }
    )
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    public Book(String title, String description/*, String uploadedUrl*/) {
        this.title = title;
        this.description = description;
//        this.uploadedUrl = uploadedUrl;
    }

    public void setChapters(List<Chapter> chapters) {
        if (chapters != null) {
            this.chapters = chapters;
            this.amount = chapters.size();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && amount == book.amount && title.equals(book.title) && description.equals(book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, amount);
    }

    @Override
    public String toString() {
        return "Book{" +
                ", title='" + title + '\'' +
                " description='" + description + '\'' +
                ", amount=" + amount + '\'' +
//                ", url='" + uploadedUrl +
                '}';
    }
}
