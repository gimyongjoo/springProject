package com.study.springStarter.mapper;

import com.study.springStarter.dto.CheckList;
import com.study.springStarter.dto.Folder;
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
public class CheckListMapperTest {

    @Autowired
    private CheckListMapper checkListMapper;
    @Autowired
    private NoteMapper noteMapper; // CheckList는 Note에 의존
    @Autowired
    private UserMapper userMapper; // Note는 User에 의존
    @Autowired
    private FolderMapper folderMapper; // Note는 Folder에 의존

    private int testNoteId;
    private int testUserId; // Note 생성을 위한 user ID
    private int testFolderId; // Note 생성을 위한 folder ID

    @BeforeEach
    void setUp() throws Exception {
        // 각 테스트 전에 필요한 사용자, 폴더, 노트 데이터 생성
        User user = new User();
        user.setName("testCheckListUser");
        user.setPwd("clpwd");
        user.setEmail("cluser@test.com");
        userMapper.insert(user);
        testUserId = user.getUserId();

        Folder folder = new Folder();
        folder.setUserId(testUserId);
        folder.setName("CheckList Test Folder");
        folderMapper.insertFolder(folder);
        testFolderId = folder.getFolderId();

        Note note = new Note();
        note.setUserId(testUserId);
        note.setFolderId(testFolderId);
        note.setTitle("CheckList Parent Note");
        note.setContent("Test content for checklist.");
        note.setPinned(false);
        note.setMarkdown_enabled(true);
        noteMapper.insertNote(note);
        testNoteId = note.getNoteId();
    }

    private CheckList createTestCheckList(String content, boolean isChecked, int noteId) {
        CheckList checkList = new CheckList();
        checkList.setNoteId(noteId);
        checkList.setContent(content);
        checkList.setChecked(isChecked);
        return checkList;
    }

    @Test
    void testInsertCheckList() throws Exception {
        CheckList checkList = createTestCheckList("첫 번째 체크리스트 항목", false, testNoteId);
        int result = checkListMapper.insertCheckList(checkList);
        Assertions.assertEquals(1, result, "체크리스트 삽입 성공");
        Assertions.assertNotNull(checkList.getCheckListId(), "삽입 후 checkListId가 설정되었는지 확인"); // XML에 useGeneratedKeys 설정 필요

        CheckList foundCheckList = checkListMapper.selectCheckListById(checkList.getCheckListId());
        Assertions.assertNotNull(foundCheckList, "삽입된 체크리스트 조회");
        Assertions.assertEquals("첫 번째 체크리스트 항목", foundCheckList.getContent(), "체크리스트 내용 일치");
    }

    @Test
    void testSelectCheckListById() throws Exception {
        CheckList checkList = createTestCheckList("조회용 체크리스트", false, testNoteId);
        checkListMapper.insertCheckList(checkList);

        CheckList foundCheckList = checkListMapper.selectCheckListById(checkList.getCheckListId());
        Assertions.assertNotNull(foundCheckList, "ID로 체크리스트 조회 성공");
        Assertions.assertEquals("조회용 체크리스트", foundCheckList.getContent());
    }

    @Test
    void testSelectCheckListsByNoteId() throws Exception {
        checkListMapper.insertCheckList(createTestCheckList("노트별 항목1", false, testNoteId));
        checkListMapper.insertCheckList(createTestCheckList("노트별 항목2", true, testNoteId));

        // 다른 노트 생성 (이 노트의 체크리스트는 조회되지 않아야 함)
        Note anotherNote = new Note();
        anotherNote.setUserId(testUserId);
        anotherNote.setFolderId(testFolderId);
        anotherNote.setTitle("Another Note for CL");
        anotherNote.setContent("Content.");
        anotherNote.setPinned(false);
        anotherNote.setMarkdown_enabled(true);
        noteMapper.insertNote(anotherNote);
        checkListMapper.insertCheckList(createTestCheckList("다른 노트 항목", false, anotherNote.getNoteId()));


        List<CheckList> checkLists = checkListMapper.selectCheckListByNoteId(testNoteId);
        Assertions.assertEquals(2, checkLists.size(), "노트 ID로 2개의 체크리스트 조회");
    }

    @Test
    void testUpdateCheckList() throws Exception {
        CheckList checkList = createTestCheckList("업데이트 전 항목", false, testNoteId);
        checkListMapper.insertCheckList(checkList);

        checkList.setContent("업데이트 후 항목");
        checkList.setChecked(true);

        int result = checkListMapper.updateCheckList(checkList);
        Assertions.assertEquals(1, result, "체크리스트 업데이트 성공");

        CheckList updatedCheckList = checkListMapper.selectCheckListById(checkList.getCheckListId());
        Assertions.assertEquals("업데이트 후 항목", updatedCheckList.getContent());
        Assertions.assertTrue(updatedCheckList.getChecked());
    }

    @Test
    void testDeleteCheckList() throws Exception {
        CheckList checkList = createTestCheckList("삭제할 항목", false, testNoteId);
        checkListMapper.insertCheckList(checkList);

        int result = checkListMapper.deleteCheckList(checkList.getCheckListId());
        Assertions.assertEquals(1, result, "체크리스트 삭제 성공");

        CheckList deletedCheckList = checkListMapper.selectCheckListById(checkList.getCheckListId());
        Assertions.assertNull(deletedCheckList, "체크리스트가 성공적으로 삭제되어 조회되지 않음");
    }
}