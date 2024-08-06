package com.shadoww.imageservice.controller;

import com.shadoww.imageservice.model.BookImage;
import com.shadoww.imageservice.service.interfac.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiImagesController.class)
public class ApiImagesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    public void testAddBookImage() throws Exception {
        BookImage mockImage = new BookImage();
        mockImage.setId(1L);

        when(imageService.create(any(BookImage.class))).thenReturn(mockImage);

        MockMultipartFile image = new MockMultipartFile("image", "test-image".getBytes());

        mockMvc.perform(multipart("/api/media/books/{id}", 1L)
                        .file(image))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }
}