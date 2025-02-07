package com.shadoww.parserservice.client;


import com.shadoww.api.dto.request.ImageRequest;
import com.shadoww.api.dto.response.ImageResponse;
import com.shadoww.jwtsecurity.client.JwtFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "media-service", configuration = JwtFeignClientConfig.class)
public interface MediaServiceClient {
    //    @PostMapping(value = "/api/media/books/{bookId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    ResponseEntity<?> addBookImage(@PathVariable("bookId") long bookId, @RequestParam("image") MultipartFile data);
    @PostMapping(value = "/api/media/books/{bookId}")
    ImageResponse addBookImage(@PathVariable("bookId") long bookId,
                               @RequestBody ImageRequest request);

    @PostMapping("/api/media/books/{bookId}/chapters/{chapterId}")
    ImageResponse addChapterImage(
            @PathVariable("bookId") long bookId,
            @PathVariable("chapterId") long chapterId,
            @RequestBody ImageRequest request);

    @PostMapping("/api/media/users/{userId}")
    ImageResponse addPersonImage(@PathVariable("userId") long userId,
                                     @RequestBody ImageRequest request);

    @GetMapping("/api/media/books/{bookId}")
    List<ImageResponse> getBookImages(@PathVariable("bookId") long bookId);

    @GetMapping("/api/media/chapters/{chapterId}")
    List<ImageResponse> getChapterImages(@PathVariable("chapterId") long chapterId);

    @GetMapping("/api/media/img/{id}")
    ImageResponse getImageById(@PathVariable("id") long id);

    @GetMapping("/api/media/{filename}")
    byte[] getBookImageData(@PathVariable("filename") String filename);

    @DeleteMapping("/api/media/{imageId}")
    ResponseEntity<?> deleteImageById(@PathVariable("imageId") long imageId);
}