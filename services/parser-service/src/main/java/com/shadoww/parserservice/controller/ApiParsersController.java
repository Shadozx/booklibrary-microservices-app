package com.shadoww.parserservice.controller;

import com.shadoww.api.dto.request.ImageRequest;
import com.shadoww.api.dto.request.book.ParseLinkRequest;
import com.shadoww.api.dto.response.ImageResponse;
import com.shadoww.parserservice.client.ImageServiceClient;
import com.shadoww.parserservice.util.formatters.BooksFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import org.springframework.mock.web.MockMultipartFile;

import javax.xml.parsers.ParserConfigurationException;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/parsers")
public class ApiParsersController {

    private final ImageServiceClient imageServiceClient;

    private final BooksFormatter booksFormatter;

    @Autowired
    public ApiParsersController(ImageServiceClient imageServiceClient, BooksFormatter booksFormatter) {
        this.imageServiceClient = imageServiceClient;
        this.booksFormatter = booksFormatter;
    }


    @PostMapping("/parse")
    public ResponseEntity<?> parse(@RequestBody ParseLinkRequest request) {

        String url = request.getUrl();

        System.out.println("Url: " + "\"" + url + "\"");

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Передано було не коректне посилання");
        }

//        if (bookService.existsByUrl(url)) {
//            return ResponseBook.exist();
//        }

        boolean isAdded = booksFormatter.format(url);
        System.out.println("Url was processed: " + isAdded);

        if (isAdded) {
            return ResponseEntity.ok("Посилання було успішно опрацьовано");
        }

        System.out.println("Error!");
//        return ResponseBook.errorServer();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Сталася помилка при опрацьовування посилання");
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/to/fb2")
    public ResponseEntity<?> parseBookUrlToFb2BookFile(@RequestBody ParseLinkRequest request) throws IOException, ParserConfigurationException {


        String url = request.getUrl();

//        System.out.println("Url: " + "\"" + url + "\"");

        System.out.printf("Url: '%s'\n", url);

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Передано було не коректне посилання");
        }


//        return ResponseEntity.ok(booksFormatter.parseToFb2(url).toByteArray());

        byte[] fb2FileContent = booksFormatter.parseToFb2(url).toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("book.fb2")
                .build());


        return ResponseEntity.ok()
                .headers(headers)
                .body(fb2FileContent);
    }

    @GetMapping("/add")
    public ResponseEntity<?> addImage(@RequestParam(value = "bId", defaultValue = "1") long bookId) throws IOException {
        ImageRequest request = new ImageRequest();
        request.setFileName("book_1.jpg");


        Resource imgFile = new ClassPathResource("images/image.png");

        System.out.println(imgFile.getFile().toPath());

        // Читаємо файл у масив байтів
        byte[] imageBytes = Files.readAllBytes(imgFile.getFile().toPath());

//        request.setData("test photo".getBytes());
        request.setData(imageBytes);

//        System.out.println((List<ImageResponse>)imageServiceClient.getBookImages(23).getBody());

//        ResponseEntity<?> response = imageServiceClient.addBookImage(bookId, request);
//
//        System.out.println(response);


        return ResponseEntity.ok().body("Add photo");
    }


    @GetMapping("/get-photo")
    public ResponseEntity<?> getPhotoImageService(@RequestParam(value = "iD", defaultValue = "20") long iD) {

        imageServiceClient.getImageById(iD);

        return ResponseEntity.ok().build();
    }

}