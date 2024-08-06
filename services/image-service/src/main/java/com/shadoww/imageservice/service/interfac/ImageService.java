package com.shadoww.imageservice.service.interfac;

//import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.imageservice.model.*;
import com.shadoww.api.service.CrudService;

import java.util.List;


public interface ImageService extends CrudService<Image, Long> {

    void createChapterImages(List<ChapterImage> images);

    Image getImageByFilename(String filename);

    List<Image> getImages(long bookId);
    List<Image> getChapterImages(long chapterId);

    void deleteByFilename(String filename);

    void deleteBookImages(Long bookId);

    void deleteChapterImages(List<ChapterImage> images);

    void deleteImagesByBookId(long bookId);

//    void deleteChaptersImages(List<Chapter> chapters);
}