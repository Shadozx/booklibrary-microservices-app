package com.shadoww.parserservice.client;

import com.shadoww.api.dto.request.AuthorRequest;
import com.shadoww.api.dto.request.ChapterRequest;
import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.api.dto.response.ChapterResponse;
import com.shadoww.jwtsecurity.client.JwtFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "library-service", configuration = JwtFeignClientConfig.class)
public interface LibraryServiceClient {

    @PostMapping("/api/books/filter")
    List<BookResponse> filterBooks(@RequestBody BookFilterRequest request);

    @PostMapping("/api/books")
    BookResponse addBook(@RequestBody BookRequest request);

    @PostMapping("/api/books/{bookId}/chapters")
    ChapterResponse addChapter(@PathVariable long bookId, @RequestBody ChapterRequest request);

    @PutMapping("/api/books/{bookId}")
    BookResponse updateBook(@PathVariable long bookId, @RequestBody BookRequest request);

    @DeleteMapping("/api/books/{bookId}")
    void deleteBook(@PathVariable long bookId);

    @DeleteMapping("/api/books/{bookId}/chapters/{chapterId}")
    void deleteChapter(@PathVariable long bookId, @PathVariable long chapterId);

    @PostMapping("/api/authors")
    AuthorResponse createAuthor(@RequestBody AuthorRequest request);

    @PutMapping("/api/authors/{id}")
    AuthorResponse updateAuthor(@PathVariable long id, @RequestBody AuthorRequest request);

    @DeleteMapping("/api/authors/{id}")
    void deleteAuthor(@PathVariable long id, @RequestBody AuthorRequest request);

    @PostMapping("/api/books/{id}/authors")
    void addAuthorToBook(@PathVariable long id, @RequestBody Map<String, Long> requestBody);

    @PostMapping("/api/authors/filter")
    List<AuthorResponse> filterAuthors(@RequestBody AuthorRequest request);

    @GetMapping("/api/authors/{authorId}/books")
    List<BookResponse> getAuthorBooks(@PathVariable long authorId);
}