package com.study.springStarter.mapper;

import com.study.springStarter.dto.Folder;
import com.study.springStarter.dto.Image;
import com.study.springStarter.dto.Note;
import com.study.springStarter.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ImageMapperTest {

    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private NoteMapper noteMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FolderMapper folderMapper;

    private int testNoteId;
    private int testUserId;
    private int testFolderId;

    @BeforeEach
    void setUp() throws Exception {
        User user = new User();
        user.setName("testImageUser");
        user.setPwd("imgpwd");
        user.setEmail("imguser@test.com");
        userMapper.insert(user);
        testUserId = user.getUserId();

        Folder folder = new Folder();
        folder.setUserId(testUserId);
        folder.setName("Image Test Folder");
        folderMapper.insertFolder(folder);
        testFolderId = folder.getFolderId();

        Note note = new Note();
        note.setUserId(testUserId);
        note.setFolderId(testFolderId);
        note.setTitle("Image Parent Note");
        note.setContent("Test content for image.");
        note.setPinned(false);
        note.setMarkdown_enabled(true);
        noteMapper.insertNote(note);
        testNoteId = note.getNoteId();
    }

    private Image createTestImage(String filePath, int noteId) {
        Image image = new Image();
        image.setNoteId(noteId);
        image.setFilePath(filePath);
        return image;
    }

    @Test
    void testInsertImage() throws Exception {
        Image image = createTestImage("/path/to/image1.jpg", testNoteId);
        int result = imageMapper.insertImage(image);
        Assertions.assertEquals(1, result, "이미지 삽입 성공");
        Assertions.assertNotNull(image.getImageId(), "삽입 후 imageId가 설정되었는지 확인"); // XML에 useGeneratedKeys 설정 필요

        Image foundImage = imageMapper.selectImageById(image.getImageId());
        Assertions.assertNotNull(foundImage, "삽입된 이미지 조회");
        Assertions.assertEquals("/path/to/image1.jpg", foundImage.getFilePath(), "파일 경로 일치");
    }

    @Test
    void testSelectImageById() throws Exception {
        Image image = createTestImage("/path/to/image_lookup.png", testNoteId);
        imageMapper.insertImage(image);

        Image foundImage = imageMapper.selectImageById(image.getImageId());
        Assertions.assertNotNull(foundImage, "ID로 이미지 조회 성공");
        Assertions.assertEquals("/path/to/image_lookup.png", foundImage.getFilePath());
    }

    @Test
    void testSelectImagesByNoteId() throws Exception {
        imageMapper.insertImage(createTestImage("/path/to/note_image_a.gif", testNoteId));
        imageMapper.insertImage(createTestImage("/path/to/note_image_b.webp", testNoteId));

        // 다른 노트 생성
        Note anotherNote = new Note();
        anotherNote.setUserId(testUserId);
        anotherNote.setFolderId(testFolderId);
        anotherNote.setTitle("Another Note for Image");
        anotherNote.setContent("Content.");
        anotherNote.setPinned(false);
        anotherNote.setMarkdown_enabled(true);
        noteMapper.insertNote(anotherNote);
        imageMapper.insertImage(createTestImage("/path/to/another_note_image.jpeg", anotherNote.getNoteId()));

        List<Image> images = imageMapper.selectImagesByNoteId(testNoteId);
        Assertions.assertEquals(2, images.size(), "노트 ID로 2개의 이미지 조회");
    }

    @Test
    void testDeleteImage() throws Exception {
        Image image = createTestImage("/path/to/delete_me.svg", testNoteId);
        imageMapper.insertImage(image);

        int result = imageMapper.deleteImage(image.getImageId());
        Assertions.assertEquals(1, result, "이미지 삭제 성공");

        Image deletedImage = imageMapper.selectImageById(image.getImageId());
        Assertions.assertNull(deletedImage, "이미지가 성공적으로 삭제되어 조회되지 않음");
    }
}