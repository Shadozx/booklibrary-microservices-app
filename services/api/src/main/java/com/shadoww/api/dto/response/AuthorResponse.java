package com.shadoww.api.dto.response;

//import com.shadoww.BookLibraryApp.model.Author;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Setter
@Getter
public class AuthorResponse {

    long id;

    String name;

    String biography;

//    String uploadedUrl;

    public AuthorResponse(long id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
//        this.uploadedUrl = uploadedUrl;
    }


    //    public AuthorResponse(Author author) {
//        this.id = author.getId();
//        this.name = author.getName();
//        this.biography = author.getBiography();
//        this.uploadedUrl = author.getUploadedUrl();
//    }
}
