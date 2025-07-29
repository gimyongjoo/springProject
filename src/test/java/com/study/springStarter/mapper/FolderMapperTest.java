package com.study.springStarter.mapper;

import com.study.springStarter.dto.Folder;
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
public class FolderMapperTest {

    @Autowired
    private FolderMapper folderMapper;
    @Autowired
    private UserMapper userMapper; // Folder는 User에 의존하므로 UserMapper 필요

    private int testUserId;

    @BeforeEach
    void setUp() throws Exception {
        // 각 테스트 전에 필요한 사용자 데이터 생성
        User user = new User();
        user.setName("testFolderUser");
        user.setPwd("folderpwd");
        user.setEmail("folderuser@test.com");
        userMapper.insert(user); // userId 자동 생성 및 DTO에 설정
        testUserId = user.getUserId();
    }

    private Folder createTestFolder(String name, int userId) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setUserId(userId);
        return folder;
    }

    @Test
    void testInsertFolder() throws Exception {
        Folder folder = createTestFolder("새 폴더", testUserId);
        int result = folderMapper.insertFolder(folder);
        Assertions.assertEquals(1, result, "폴더 삽입 성공");
        Assertions.assertNotNull(folder.getFolderId(), "삽입 후 folderId가 설정되었는지 확인"); // XML에 useGeneratedKeys 설정 필요

        Folder foundFolder = folderMapper.selectFolderById(folder.getFolderId());
        Assertions.assertNotNull(foundFolder, "삽입된 폴더 조회");
        Assertions.assertEquals("새 폴더", foundFolder.getName(), "폴더 이름 일치");
    }

    @Test
    void testSelectFolderById() throws Exception {
        Folder folder = createTestFolder("조회용 폴더", testUserId);
        folderMapper.insertFolder(folder);

        Folder foundFolder = folderMapper.selectFolderById(folder.getFolderId());
        Assertions.assertNotNull(foundFolder, "ID로 폴더 조회 성공");
        Assertions.assertEquals("조회용 폴더", foundFolder.getName());
    }

    @Test
    void testSelectFoldersByUserId() throws Exception {
        folderMapper.insertFolder(createTestFolder("유저 폴더1", testUserId));
        folderMapper.insertFolder(createTestFolder("유저 폴더2", testUserId));

        List<Folder> folders = folderMapper.selectFoldersByUserId(testUserId);
        Assertions.assertEquals(2, folders.size(), "유저 ID로 2개의 폴더 조회");
    }

    @Test
    void testUpdateFolder() throws Exception {
        Folder folder = createTestFolder("업데이트 전 폴더", testUserId);
        folderMapper.insertFolder(folder);

        folder.setName("업데이트 후 폴더");
        int result = folderMapper.updateFolder(folder);
        Assertions.assertEquals(1, result, "폴더 업데이트 성공");

        Folder updatedFolder = folderMapper.selectFolderById(folder.getFolderId());
        Assertions.assertEquals("업데이트 후 폴더", updatedFolder.getName());
    }

    @Test
    void testDeleteFolder() throws Exception {
        Folder folder = createTestFolder("삭제할 폴더", testUserId);
        folderMapper.insertFolder(folder);

        int result = folderMapper.deleteFolder(folder.getFolderId());
        Assertions.assertEquals(1, result, "폴더 삭제 성공");

        Folder deletedFolder = folderMapper.selectFolderById(folder.getFolderId());
        Assertions.assertNull(deletedFolder, "폴더가 성공적으로 삭제되어 조회되지 않음");
    }
}