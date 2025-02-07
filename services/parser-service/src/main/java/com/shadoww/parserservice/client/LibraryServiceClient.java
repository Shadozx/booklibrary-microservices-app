package com.shadoww.parserservice.client;

import com.shadoww.api.dto.request.ChapterRequest;
import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.api.dto.response.ChapterResponse;
import com.shadoww.jwtsecurity.client.JwtFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "library-service", configuration = JwtFeignClientConfig.class)
public interface LibraryServiceClient {

    @GetMapping("/api/books/filter")
    List<BookResponse> filterBooks(@RequestBody BookFilterRequest request);

    @PostMapping("/api/books")
    BookResponse addBook(@RequestBody BookRequest request);

    @PostMapping("/api/books/{bookId}/chapters")
    ChapterResponse addChapter(@PathVariable long bookId, @RequestBody ChapterRequest request);

    @PutMapping("/api/books/{bookId}")
    BookResponse updateBook(@PathVariable long bookId, @RequestBody BookRequest request);
}