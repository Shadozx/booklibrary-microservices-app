package com.shadoww.imageservice.controller;


import com.shadoww.api.dto.request.ImageRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@PreAuthorize("hasRole('ADMIN')")
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

//    @PostMapping("/books/{bookId}")
//    public ResponseEntity<?> addBookImage(@PathVariable long bookId,
//                                          @RequestParam("image") MultipartFile data
//                                          /*@RequestBody ImageRequest request*/) throws IOException {
////        BookImage image = (BookImage) imageMapper.dtoToModel(request);
//        BookImage image = new BookImage();
//
//        image.setData(data.getBytes());
//        image.setBook(bookId);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(
//                        imageMapper.dtoToResponse(imageService.create(image))
//                );
//    }

    // for test endpoint
    @GetMapping
    public ResponseEntity<?> getAllImages() throws IOException {

        return ResponseEntity.ok(
                imageService.getAll()
                        .stream()
                        .map(imageMapper::dtoToResponse)
                        .toList()
        );
    }

    @PostMapping("/books/{bookId}")
    public ResponseEntity<?> addBookImage(@PathVariable long bookId,
                                          @RequestBody ImageRequest request
                                          /*@RequestBody ImageRequest request*/) throws IOException {
//        BookImage image = (BookImage) imageMapper.dtoToModel(request);
        System.out.println("Start adding book image in image-service");
        BookImage image = new BookImage();

        image.setData(request.getData());
        image.setBook(bookId);

        System.out.println(image);

        System.out.println("End adding book image in image-service");



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
            @RequestBody ImageRequest request) throws IOException {

        System.out.println("Chapter id:" + chapterId + " and book id: "+ bookId);
        System.out.println("FileName:" + request.getFileName());
        System.out.println("Len image:" + request.getData().length);
        ChapterImage image = new ChapterImage();
        //(ChapterImage) imageMapper.dtoToModel(request);

        image.setFileName(request.getFileName());
        image.setBookId(bookId);
//        image.setChapterImage(bookId);
        image.setChapterId(chapterId);
        image.setData(request.getData());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        imageMapper.dtoToResponse(imageService.create(image))
                );
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/users/{userId}")
    public ResponseEntity<?> addPersonImage(
            @PathVariable long userId,
            @RequestBody ImageRequest request) throws IOException {
        PersonImage image = new PersonImage();
        //(ChapterImage) imageMapper.dtoToModel(request);

        image.setOwnerId(userId);
        image.setData(request.getData());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        imageMapper.dtoToResponse(imageService.create(image))
                );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/books/{bookId}")
    public ResponseEntity<?> getBookImages(@PathVariable long bookId) {
        return ResponseEntity.ok(
                imageService.getBookImages(bookId)
                        .stream()
                        .map(imageMapper::dtoToResponse)
                        .toList()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/chapters/{chapterId}")
    public ResponseEntity<?> getChapterImages(@PathVariable long chapterId) {
        return ResponseEntity.ok(
                imageService.getChapterImages(chapterId)
                        .stream()
                        .map(imageMapper::dtoToResponse)
                        .toList()
        );
    }


    @PreAuthorize("true")
    @GetMapping("/img/{id}")
    public ResponseEntity<?> getImageById(@PathVariable long id) {
        Image image = imageService.readById(id);

        return ResponseEntity.ok(imageMapper.dtoToResponse(image));
    }

    @PreAuthorize("true")
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