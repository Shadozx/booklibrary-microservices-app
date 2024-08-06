package com.shadoww.imageservice.controller;


import com.shadoww.imageservice.mapper.ImageMapper;
import com.shadoww.imageservice.model.BookImage;
import com.shadoww.imageservice.model.ChapterImage;
import com.shadoww.imageservice.model.Image;
import com.shadoww.imageservice.model.PersonImage;
import com.shadoww.imageservice.service.interfac.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/media")
public class ApiImagesController {

    private final ImageService imageService;

    private final ImageMapper imageMapper;

    @Autowired
    public ApiImagesController(ImageService imageService, ImageMapper imageMapper) {
        this.imageService = imageService;
        this.imageMapper = imageMapper;
    }

    @PostMapping("/books/{bookId}")
    public ResponseEntity<?> addBookImage(@PathVariable long bookId,
                                          @RequestParam("image") MultipartFile data
                                          /*@RequestBody ImageRequest request*/) throws IOException {
//        BookImage image = (BookImage) imageMapper.dtoToModel(request);
        BookImage image = new BookImage();

        image.setData(data.getBytes());
        image.setBook(bookId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        imageMapper.dtoToResponse(imageService.create(image))
                );
    }

    @PostMapping("/books/{bookId}/chapters/{chapterId}")
    public ResponseEntity<?> addChapterImage(
            @PathVariable long bookId,
            @PathVariable long chapterId,
            @RequestParam("image") MultipartFile data
            /*@RequestBody ImageRequest request*/) throws IOException {
        ChapterImage image = new ChapterImage();
        //(ChapterImage) imageMapper.dtoToModel(request);

        image.setChapterImage(bookId);
        image.setChapterId(chapterId);
        image.setData(data.getBytes());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        imageMapper.dtoToResponse(imageService.create(image))
                );
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<?> addPersonImage(
            @PathVariable long userId,
            @RequestParam("image") MultipartFile data
            /*@RequestBody ImageRequest request*/) throws IOException {
        PersonImage image = new PersonImage();
        //(ChapterImage) imageMapper.dtoToModel(request);

        image.setOwnerId(userId);
        image.setData(data.getBytes());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        imageMapper.dtoToResponse(imageService.create(image))
                );
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<?> getBookImages(@PathVariable long bookId) {
        return ResponseEntity.ok(
                imageService.getImages(bookId)
                        .stream()
                        .map(imageMapper::dtoToResponse)
                        .toList()
        );
    }

    @GetMapping("/chapters/{chapterId}")
    public ResponseEntity<?> getChapterImages(@PathVariable long chapterId) {
        return ResponseEntity.ok(
                imageService.getChapterImages(chapterId)
                        .stream()
                        .map(imageMapper::dtoToResponse)
                        .toList()
        );
    }

    @GetMapping("/{filename}")
    public ResponseEntity<?> getBookImageData(@PathVariable String filename) {
        Image image = imageService.getImageByFilename(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getData().length)
                .body(image.getData());
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImageById(@PathVariable long imageId) {
        imageService.deleteById(imageId);

        return ResponseEntity.ok().build();
    }
}