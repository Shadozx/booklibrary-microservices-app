package com.shadoww.mediaservice.service;

import com.shadoww.mediaservice.model.BookImage;
import com.shadoww.mediaservice.service.interfac.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ImageServiceTests {

    private final ImageService imageService;

    @Autowired
    public ImageServiceTests(ImageService imageService) {
        this.imageService = imageService;
    }

    @Test
    public void testCreateMethod() {
        BookImage image = new BookImage();
        image.setData("test data".getBytes());
        image.setBook(1L);


        imageService.create(image);

        assertEquals(1, imageService.count(), "Image must be one!");
    }
}
