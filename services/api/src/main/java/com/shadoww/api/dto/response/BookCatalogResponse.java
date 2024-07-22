package com.shadoww.api.dto.response;

//import com.shadoww.BookLibraryApp.model.BookCatalog;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Setter
@Getter
public class BookCatalogResponse {

    long id;

    String title;

    boolean isPublic;

    long ownerId;

//    public BookCatalogResponse(BookCatalog catalog) {
//        this.id = catalog.getId();
//        this.isPublic = catalog.getIsPublic();
//        this.title = catalog.getTitle();
//        this.ownerId = catalog.getOwner().getId();
//    }
    public boolean getIsPublic() {
        return isPublic;
    }
}
