package com.shadoww.parserservice.service;

import com.shadoww.api.dto.request.AuthorRequest;
import com.shadoww.api.dto.request.BookSeriesRequest;
import com.shadoww.api.dto.request.ChapterRequest;
import com.shadoww.api.dto.request.book.BookFilterRequest;
import com.shadoww.api.dto.request.book.BookRequest;
import com.shadoww.api.dto.response.AuthorResponse;
import com.shadoww.api.dto.response.BookResponse;
import com.shadoww.api.dto.response.BookSeriesResponse;
import com.shadoww.api.dto.response.ChapterResponse;
import com.shadoww.parserservice.client.LibraryServiceClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Service
public class RetryableLibraryService {

    private final LibraryServiceClient libraryServiceClient;

    @Autowired
    public RetryableLibraryService(LibraryServiceClient libraryServiceClient) {
        this.libraryServiceClient = libraryServiceClient;
    }


    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<BookResponse> filterBooks(BookFilterRequest request) {
        return libraryServiceClient.filterBooks(request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public BookResponse addBook(BookRequest request) {
        return libraryServiceClient.addBook(request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public ChapterResponse addChapter(long bookId, ChapterRequest request) {
        return libraryServiceClient.addChapter(bookId, request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public BookResponse updateBook(long bookId, BookRequest request) {
        return libraryServiceClient.updateBook(bookId, request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void deleteBook(long bookId) {
        libraryServiceClient.deleteBook(bookId);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void deleteChapter(long bookId, long chapterId) {
        libraryServiceClient.deleteChapter(bookId, chapterId);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public AuthorResponse addAuthor(AuthorRequest request) {
        return libraryServiceClient.createAuthor(request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public AuthorResponse updateAuthor(long id, AuthorRequest request) {
        return libraryServiceClient.updateAuthor(id, request);
    }


    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<AuthorResponse> filterAuthors(AuthorRequest request) {
        return libraryServiceClient.filterAuthors(request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void addAuthorToBook(long bookId, long authorId) {
        libraryServiceClient.addAuthorToBook(bookId, Map.of("author_id", authorId));
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<BookResponse> getAuthorBooks(long authorId) {
        return libraryServiceClient.getAuthorBooks(authorId);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public BookSeriesResponse addBookSeries(BookSeriesRequest request) {
        return libraryServiceClient.createBookSeries(request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public BookSeriesResponse updateBookSeries(long id, BookSeriesRequest request) {
        return libraryServiceClient.updateBookSeries(id, request);
    }


    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<BookSeriesResponse> filterBookSeries(BookSeriesRequest request) {
        return libraryServiceClient.filterBookSeries(request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void addBookSeriesToBook(long bookId, long seriesId) {
        libraryServiceClient.addBookSeriesToBook(bookId, Map.of("bookseries_id", seriesId));
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<BookResponse> getBookSeriesBooks(long seriesId) {
        return libraryServiceClient.getBookSeriesBooks(seriesId);
    }
}