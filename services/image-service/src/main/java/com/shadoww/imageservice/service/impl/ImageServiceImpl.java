package com.shadoww.imageservice.service.impl;

import com.shadoww.api.exception.NotFoundException;
import com.shadoww.imageservice.model.*;
import com.shadoww.imageservice.repository.BookImageRepository;
import com.shadoww.imageservice.repository.ChapterImageRepository;
import com.shadoww.imageservice.repository.ImageRepository;
import com.shadoww.imageservice.service.interfac.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    private final BookImageRepository bookImageRepository;

    private final ChapterImageRepository chapterImageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, BookImageRepository bookImageRepository, ChapterImageRepository chapterImageRepository) {
        this.imageRepository = imageRepository;
        this.bookImageRepository = bookImageRepository;
        this.chapterImageRepository = chapterImageRepository;
    }

    @Override
    @Transactional
    public Image create(Image image) {

        checkIsImageNull(image);

        return save(image);
    }

    @Override
    @Transactional
    public void createChapterImages(List<ChapterImage> images) {
        if (images != null && !images.isEmpty()) {
            for (var image : images) {
                create(image);
            }
        }
    }

    @Override
    public Image readById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Немає такої фотографії"));
    }

    @Override
    public boolean existsById(Long id) {
        return imageRepository.existsById(id);
    }

    @Override
    public Image getImageByFilename(String filename) {
        return imageRepository.findImageByFileName(filename)
                .orElseThrow(() -> new NotFoundException("Немає такої фотографії"));
    }

    @Override
    public List<Image> getImages(long bookId) {
        List<Image> images = chapterImageRepository.findByBookId(bookId)
                .stream()
                .map(i -> (Image) i)
                .toList();

        Optional<BookImage> bookImage = bookImageRepository.findBookImageByBookId(bookId);
        bookImage.ifPresent(images::add);

        return images;
    }

    @Override
    public List<Image> getChapterImages(long chapterId) {
        return chapterImageRepository.findByChapterId(chapterId)
                .stream()
                .map(i -> (Image) i)
                .toList();
    }

    @Override
    public Image update(Image updatedImage) {
        checkIsImageNull(updatedImage);

        readById(updatedImage.getId());

        return save(updatedImage);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        delete(readById(id));
    }

    @Override
    public long count() {
        return imageRepository.count();
    }

    @Override
    @Transactional
    public void deleteByFilename(String filename) {
        imageRepository.deleteImageByFileName(filename);
    }

    @Override
    @Transactional
    public void deleteChapterImages(List<ChapterImage> images) {
        if (images != null && !images.isEmpty())
            for (var image : images)
                delete(image);
    }

    @Override
    @Transactional
    public void deleteImagesByBookId(long bookId) {

        bookImageRepository.deleteByBookId(bookId);
        chapterImageRepository.deleteByBookId(bookId);
    }

//    @Override
//    @Transactional
//    public void deleteChaptersImages(List<Chapter> chapters) {
//        if (chapters != null) {
//            for (var c : chapters) {
//                deleteChapterImages(c.getImages());
//            }
//        }
//    }

    @Override
    @Transactional
    public void deleteBookImages(Long bookId) {
        bookImageRepository.deleteByBookId(bookId);
    }

    @Override
    public List<Image> getAll() {
        return imageRepository.findAll();
    }

    @Transactional
    Image save(Image image) {

        checkIsImageNull(image);

        return imageRepository.save(image);
    }

    @Transactional
    void delete(Image image) {
        checkIsImageNull(image);

        imageRepository.delete(image);
    }

    private void checkIsImageNull(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Фотографія не може бути пустою");
        }
    }
}