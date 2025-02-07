package com.shadoww.mediaservice.model;


//import com.shadoww.BookLibraryApp.model.user.Person;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("User")
@Setter
@Getter
@NoArgsConstructor
public class PersonImage extends Image {

    @Column(name = "owner_id")
    private Long ownerId;
}