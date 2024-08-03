package com.shadoww.parserservice.controller;

import com.shadoww.api.dto.request.book.ParseLinkRequest;
import com.shadoww.parserservice.util.formatters.BooksFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/api/parsers")
public class ApiParsersController {


    private final BooksFormatter booksFormatter;

    @Autowired
    public ApiParsersController(BooksFormatter booksFormatter) {
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
}
