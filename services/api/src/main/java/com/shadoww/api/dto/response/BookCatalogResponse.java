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


    public BookCatalogResponse(long id, String title, boolean isPublic, long ownerId) {
        this.id = id;
        this.title = title;
        this.isPublic = isPublic;
        this.ownerId = ownerId;
    }

    public boolean getIsPublic() {
        return isPublic;
    }
}
