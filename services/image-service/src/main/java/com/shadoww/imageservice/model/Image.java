package com.shadoww.imageservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
@Getter
@Setter
@NoArgsConstructor
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // default value content type is image/jpeg
    @NotBlank(message = "Content type cannot be empty")
    private String contentType = "image/jpeg";

    @NotNull(message = "Image must have data")
    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] data;

    @NotBlank(message = "Filename cannot be empty")
    @Column(unique = true, nullable = false)
    private String fileName;

    public Image(String filename) {
        this.fileName = filename;
    }

}