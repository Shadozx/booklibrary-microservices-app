package com.shadoww.parserservice.service;

import com.shadoww.api.dto.request.ImageRequest;
import com.shadoww.api.dto.response.ImageResponse;
import com.shadoww.parserservice.client.MediaServiceClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class RetryableMediaService {

    private final MediaServiceClient mediaServiceClient;

    @Autowired
    public RetryableMediaService(MediaServiceClient mediaServiceClient) {
        this.mediaServiceClient = mediaServiceClient;
    }


    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public ImageResponse addBookImage(long bookId, ImageRequest request) {
        return mediaServiceClient.addBookImage(bookId, request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public ImageResponse addChapterImage(long bookId, long chapterId, ImageRequest request) {
        return mediaServiceClient.addChapterImage(bookId, chapterId, request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public ImageResponse addPersonImage(long userId, ImageRequest request) {
        return mediaServiceClient.addPersonImage(userId, request);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<ImageResponse> getBookImages(long bookId) {
        return mediaServiceClient.getBookImages(bookId);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<ImageResponse> getChapterImages(long chapterId) {
        return mediaServiceClient.getChapterImages(chapterId);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public ImageResponse getImageById(@PathVariable("id") long id) {
        return mediaServiceClient.getImageById(id);
    }

    @Retryable(value = {FeignException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public ResponseEntity<?> deleteImageById(long imageId) {
        return mediaServiceClient.deleteImageById(imageId);
    }
}
