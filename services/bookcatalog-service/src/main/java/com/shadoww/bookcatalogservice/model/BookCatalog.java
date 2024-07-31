package com.shadoww.bookcatalogservice.model;


//import com.shadoww.bookcatalogservice.dto.request.BookCatalogRequest;
//import com.shadoww.BookLibraryApp.model.user.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class BookCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Title cannot be empty")
    @Column(nullable = false)
    private String title;

    // default catalog's visibility is public
    private boolean isPublic = true;

    @NotBlank(message = "Catalog must have owner")
    @Column(name = "owner_id")
    private Long ownerId;

    @OneToMany(mappedBy = "catalog")
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    private List<BookMark> bookMarks;


//    public BookCatalog(BookCatalogRequest bookCatalogRequest) {
//        this.setTitle(bookCatalogRequest.getTitle());
//        this.setIsPublic(bookCatalogRequest.getIsPublic());
//    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    public boolean getIsPublic() {
        return isPublic;
    }

}
