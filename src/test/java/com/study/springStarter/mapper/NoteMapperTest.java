package com.study.springStarter.mapper;

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
public class NoteMapperTest {

    @Autowired
    private NoteMapper noteMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FolderMapper folderMapper;

    // 테스트를 위한 사용자 및 폴더 ID
    private int testUserId;
    private int testFolderId;

    @BeforeEach
    void setUp() throws Exception {
        // 각 테스트 전에 필요한 사용자 및 폴더 데이터 생성
        User user = new User();
        user.setName("testNoteUser");
        user.setPwd("pwd123");
        user.setEmail("noteuser@test.com");
        userMapper.insert(user); // userId 자동 생성
        testUserId = user.getUserId(); // 생성된 ID 사용

        Folder folder = new Folder();
        folder.setUserId(testUserId);
        folder.setName("Test Folder");
        folderMapper.insertFolder(folder); // folderId 자동 생성
        testFolderId = folder.getFolderId(); // 생성된 ID 사용
    }

    private Note createTestNote(String title, String content, boolean isPinned, Integer folderId) {
        Note note = new Note();
        note.setUserId(testUserId);
        note.setFolderId(folderId); // null 가능
        note.setTitle(title);
        note.setContent(content);
        note.setPinned(isPinned);
        note.setMarkdown_enabled(true);
        return note;
    }

    @Test
    void testInsertNote() throws Exception {
        Note note = createTestNote("새 노트 제목", "새 노트 내용", false, testFolderId);
        int result = noteMapper.insertNote(note);
        Assertions.assertEquals(1, result, "노트 삽입 성공");
        Assertions.assertNotNull(note.getNoteId(), "삽입 후 noteId가 설정되었는지 확인"); // XML에 useGeneratedKeys 설정 필요

        Note foundNote = noteMapper.selectNoteById(note.getNoteId());
        Assertions.assertNotNull(foundNote, "삽입된 노트 조회");
        Assertions.assertEquals("새 노트 제목", foundNote.getTitle(), "노트 제목 일치");
    }

    @Test
    void testSelectNoteById() throws Exception {
        Note note = createTestNote("조회용 노트", "조회용 내용", false, null); // 폴더 없이
        noteMapper.insertNote(note);

        Note foundNote = noteMapper.selectNoteById(note.getNoteId());
        Assertions.assertNotNull(foundNote, "ID로 노트 조회 성공");
        Assertions.assertEquals("조회용 노트", foundNote.getTitle());
    }

    @Test
    void testSelectNotesByUserId() throws Exception {
        noteMapper.insertNote(createTestNote("유저별 노트1", "내용1", false, testFolderId));
        noteMapper.insertNote(createTestNote("유저별 노트2", "내용2", true, null));

        List<Note> notes = noteMapper.selectNotesByUserId(testUserId);
        Assertions.assertEquals(2, notes.size(), "유저 ID로 2개의 노트 조회");
    }

    @Test
    void testSelectNotesByFolderId() throws Exception {
        // 다른 폴더 생성 (선택 사항: 여러 폴더에 노트가 있을 경우)
        Folder anotherFolder = new Folder();
        anotherFolder.setUserId(testUserId);
        anotherFolder.setName("Another Folder");
        folderMapper.insertFolder(anotherFolder);
        int anotherFolderId = anotherFolder.getFolderId();

        noteMapper.insertNote(createTestNote("폴더1 노트1", "내용", false, testFolderId));
        noteMapper.insertNote(createTestNote("폴더1 노트2", "내용", true, testFolderId));
        noteMapper.insertNote(createTestNote("다른 폴더 노트", "내용", false, anotherFolderId));

        List<Note> notes = noteMapper.selectNotesByFolderId(testFolderId);
        Assertions.assertEquals(2, notes.size(), "특정 폴더 ID로 2개의 노트 조회");
        Assertions.assertTrue(notes.stream().allMatch(n -> n.getFolderId() != null && n.getFolderId() == testFolderId));
    }

    @Test
    void testSelectPinnedNotes() throws Exception {
        noteMapper.insertNote(createTestNote("핀 노트1", "내용", true, testFolderId));
        noteMapper.insertNote(createTestNote("핀 노트2", "내용", true, null));
        noteMapper.insertNote(createTestNote("일반 노트", "내용", false, testFolderId));

        List<Note> pinnedNotes = noteMapper.selectPinnedNotes(testUserId);
        Assertions.assertEquals(2, pinnedNotes.size(), "핀 고정된 노트 2개 조회");
        Assertions.assertTrue(pinnedNotes.stream().allMatch(Note::getPinned), "모든 노트가 핀 고정되어 있는지 확인");
    }

    @Test
    void testUpdateNote() throws Exception {
        Note note = createTestNote("업데이트 전", "내용", false, testFolderId);
        noteMapper.insertNote(note);

        note.setTitle("업데이트 후 제목");
        note.setContent("업데이트 후 내용");
        note.setPinned(true);

        int result = noteMapper.updateNote(note);
        Assertions.assertEquals(1, result, "노트 업데이트 성공");

        Note updatedNote = noteMapper.selectNoteById(note.getNoteId());
        Assertions.assertEquals("업데이트 후 제목", updatedNote.getTitle());
        Assertions.assertEquals("업데이트 후 내용", updatedNote.getContent());
        Assertions.assertTrue(updatedNote.getPinned());
    }

    @Test
    void testDeleteNote() throws Exception {
        Note note = createTestNote("삭제할 노트", "삭제할 내용", false, testFolderId);
        noteMapper.insertNote(note);

        int result = noteMapper.deleteNote(note.getNoteId());
        Assertions.assertEquals(1, result, "노트 삭제 성공");

        Note deletedNote = noteMapper.selectNoteById(note.getNoteId());
        Assertions.assertNull(deletedNote, "노트가 성공적으로 삭제되어 조회되지 않음");
    }
}