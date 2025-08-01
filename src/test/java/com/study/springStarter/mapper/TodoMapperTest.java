//package com.study.springStarter.mapper;
//
//import com.study.springStarter.dto.Folder;
//import com.study.springStarter.dto.Note;
//import com.study.springStarter.dto.Todo;
//import com.study.springStarter.dto.User;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//public class TodoMapperTest {
//
//    @Autowired
//    private TodoMapper todoMapper;
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private NoteMapper noteMapper;
//    @Autowired
//    private FolderMapper folderMapper;
//
//    private int testUserId;
//    private int testNoteId; // Todo는 Note에 연결될 수 있음 (nullable)
//    private int testFolderId;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        User user = new User();
//        user.setName("testTodoUser");
//        user.setPwd("todopwd");
//        user.setEmail("todouser@test.com");
//        userMapper.insert(user);
//        testUserId = user.getUserId();
//
//        Folder folder = new Folder();
//        folder.setUserId(testUserId);
//        folder.setName("Todo Test Folder");
//        folderMapper.insertFolder(folder);
//        testFolderId = folder.getFolderId();
//
//        Note note = new Note();
//        note.setUserId(testUserId);
//        note.setFolderId(testFolderId);
//        note.setTitle("Todo Parent Note");
//        note.setContent("Test content for todo.");
//        note.setPinned(false);
//        note.setMarkdown_enabled(true);
//        noteMapper.insertNote(note);
//        testNoteId = note.getNoteId();
//    }
//
//    private Todo createTestTodo(String content, boolean isDone, LocalDateTime dueDate, Integer noteId) {
//        Todo todo = new Todo();
//        todo.setUserId(testUserId);
//        todo.setNoteId(noteId); // null 가능
//        todo.setContent(content);
//        todo.setDone(isDone);
//        if (dueDate != null) {
//            todo.setDueDate(dueDate);
//        }
//        return todo;
//    }
//
//    @Test
//    void testInsertTodo() throws Exception {
//        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
//        Todo todo = createTestTodo("새 할 일", false, tomorrow, testNoteId);
//        int result = todoMapper.insertTodo(todo);
//        Assertions.assertEquals(1, result, "할 일 삽입 성공");
//        Assertions.assertNotNull(todo.getTodoId(), "삽입 후 todoId가 설정되었는지 확인"); // XML에 useGeneratedKeys 설정 필요
//
//        Todo foundTodo = todoMapper.selectTodoById(todo.getTodoId());
//        Assertions.assertNotNull(foundTodo, "삽입된 할 일 조회");
//        Assertions.assertEquals("새 할 일", foundTodo.getContent(), "할 일 내용 일치");
//        Assertions.assertFalse(foundTodo.getDone(), "완료 상태 일치");
//    }
//
//    @Test
//    void testSelectTodoById() throws Exception {
//        Todo todo = createTestTodo("조회용 할 일", false, null, null); // 노트 없이
//        todoMapper.insertTodo(todo);
//
//        Todo foundTodo = todoMapper.selectTodoById(todo.getTodoId());
//        Assertions.assertNotNull(foundTodo, "ID로 할 일 조회 성공");
//        Assertions.assertEquals("조회용 할 일", foundTodo.getContent());
//    }
//
//    @Test
//    void testSelectTodosByUserId() throws Exception {
//        todoMapper.insertTodo(createTestTodo("유저 할 일1", false, null, testNoteId));
//        todoMapper.insertTodo(createTestTodo("유저 할 일2", true, null, null));
//
//        List<Todo> todos = todoMapper.selectTodosByUserId(testUserId);
//        Assertions.assertEquals(2, todos.size(), "유저 ID로 2개의 할 일 조회");
//    }
//
//    @Test
//    void testSelectTodosByNoteId() throws Exception {
//        todoMapper.insertTodo(createTestTodo("노트별 할 일1", false, null, testNoteId));
//        todoMapper.insertTodo(createTestTodo("노트별 할 일2", true, null, testNoteId));
//
//        // 다른 노트 생성
//        Note anotherNote = new Note();
//        anotherNote.setUserId(testUserId);
//        anotherNote.setFolderId(testFolderId);
//        anotherNote.setTitle("Another Note for Todo");
//        anotherNote.setContent("Content.");
//        anotherNote.setPinned(false);
//        anotherNote.setMarkdown_enabled(true);
//        noteMapper.insertNote(anotherNote);
//        todoMapper.insertTodo(createTestTodo("다른 노트의 할 일", false, null, anotherNote.getNoteId()));
//
//        List<Todo> todos = todoMapper.selectTodosByNoteId(testNoteId);
//        Assertions.assertEquals(2, todos.size(), "노트 ID로 2개의 할 일 조회");
//    }
//
//    @Test
//    void testUpdateTodo() throws Exception {
//        Todo todo = createTestTodo("업데이트 전", false, null, testNoteId);
//        todoMapper.insertTodo(todo);
//
//        todo.setContent("업데이트 후 내용");
//        todo.setDone(true);
//        todo.setDueDate(LocalDateTime.now().plusDays(5));
//
//        int result = todoMapper.updateTodo(todo);
//        Assertions.assertEquals(1, result, "할 일 업데이트 성공");
//
//        Todo updatedTodo = todoMapper.selectTodoById(todo.getTodoId());
//        Assertions.assertEquals("업데이트 후 내용", updatedTodo.getContent());
//        Assertions.assertTrue(updatedTodo.getDone());
//        Assertions.assertNotNull(updatedTodo.getDueDate());
//    }
//
//    @Test
//    void testDeleteTodo() throws Exception {
//        Todo todo = createTestTodo("삭제할 할 일", false, null, testNoteId);
//        todoMapper.insertTodo(todo);
//
//        int result = todoMapper.deleteTodo(todo.getTodoId());
//        Assertions.assertEquals(1, result, "할 일 삭제 성공");
//
//        Todo deletedTodo = todoMapper.selectTodoById(todo.getTodoId());
//        Assertions.assertNull(deletedTodo, "할 일이 성공적으로 삭제되어 조회되지 않음");
//    }
//}