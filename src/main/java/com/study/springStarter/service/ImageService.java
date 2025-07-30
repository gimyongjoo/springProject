package com.study.springStarter.service;

import com.study.springStarter.dto.Image;

import java.util.List;

public interface ImageService {

    int addImage(Image image) throws Exception;

    Image findImageById(int imageId) throws Exception;

    List<Image> findImagesByNoteId(int noteId) throws Exception;

    int deleteImage(int imageId) throws Exception;

}
