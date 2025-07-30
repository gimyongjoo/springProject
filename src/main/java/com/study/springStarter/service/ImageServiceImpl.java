package com.study.springStarter.service;

import com.study.springStarter.dto.Image;
import com.study.springStarter.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageMapper mapper;

    @Override
    public int addImage(Image image) throws Exception {
        return mapper.insertImage(image);
    }

    @Override
    public Image findImageById(int imageId) throws Exception {
        return mapper.selectImageById(imageId);
    }

    @Override
    public List<Image> findImagesByNoteId(int noteId) throws Exception {
        return mapper.selectImagesByNoteId(noteId);
    }

    @Override
    public int deleteImage(int imageId) throws Exception {
        return mapper.deleteImage(imageId);
    }
}
