package com.shadoww.libraryservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ім'я автора
     **/
    @NotBlank(message = "Name of the author cannot be empty")
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * Біографія автора
     **/
    @Column(columnDefinition = "varchar")
    private String biography;

    /**
     * Звідки був взятий автор (силка) а якщо пусто то значить власноруч було додано
     **/
//    @Column(name = "uploaded_url")
//    private String uploadedUrl;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && name.equals(author.name) && Objects.equals(biography, author.biography);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, biography);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", biography='" + biography + '\'' +
//                ", url='" + uploadedUrl + '\'' +
                '}';
    }
}
