package com.study.springStarter.mapper;

import com.study.springStarter.dto.Image;

import java.util.List;

public interface ImageMapper {

    int insertImage(Image image) throws Exception;

    Image selectImageById(int imageId) throws Exception;

    List<Image> selectImagesByNoteId(int noteId) throws Exception;

    int deleteImage(int imageId) throws Exception;

}
