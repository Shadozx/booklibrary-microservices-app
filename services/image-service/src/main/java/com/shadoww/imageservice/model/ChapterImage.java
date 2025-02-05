package com.shadoww.imageservice.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
//@Table(name = "chapter_images")

@DiscriminatorValue("Chapter")
@Setter
@Getter
@NoArgsConstructor
public class ChapterImage extends BookImage {

//    @NotBlank(message = "Chapter id cannot be empty")
    @NotNull
    @Column(name = "chapter_id")
    private Long chapterId;

    public ChapterImage(String filename) {
        this.setFileName(filename);
    }

    public ChapterImage(Image image) {
        this.setContentType( image.getContentType());
        this.setData(image.getData());
    }

    public void setChapterImage(Long bookId) {
        if (bookId != null) {
            this.setFileName(bookId + "_" + UUID.randomUUID() + ".jpeg");
        }
    }
}
