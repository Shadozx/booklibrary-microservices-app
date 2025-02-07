package com.shadoww.mediaservice.repository;

import com.shadoww.mediaservice.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findImageByFileName(String filename);


//    void deleteImageByBook_Id(int bookId);

    void deleteImageByFileName(String filename);

//    List<Image> findByImage_type(String imageType);
}