package com.shadoww.feedbackservice.model.comment;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Book")
@Setter
@Getter
public class BookComment extends Comment {

    @Column(name = "book_id", nullable = false)
    private Long bookId;

}