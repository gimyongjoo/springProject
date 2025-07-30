package com.study.springStarter.mapper;

import com.study.springStarter.dto.Image;

import java.util.List;

public interface ImageMapper {

    int insertImage(Image image) throws Exception; // 이미지 추가

    Image selectImageById(int imageId) throws Exception; // 이미지 조회

    List<Image> selectImagesByNoteId(int noteId) throws Exception;

    int deleteImage(int imageId) throws Exception; // 이미지 삭제

}
