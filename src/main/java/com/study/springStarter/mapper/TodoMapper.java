package com.study.springStarter.mapper;

import com.study.springStarter.dto.Todo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TodoMapper {
    int insertTodo(Todo todo); // 할 일 추가
    // noteId가 Integer이므로 @Param에 Integer로 전달
    List<Todo> selectTodosByNoteId(@Param("noteId") Integer noteId, @Param("userId") int userId); // 특정 노트의 할 일 목록 조회
    Todo selectTodoById(@Param("todoId") int todoId); // 할 일 단건 조회
    int updateTodo(Todo todo); // 할 일 수정 (내용, 완료 여부 등)
    int deleteTodo(@Param("todoId") int todoId, @Param("userId") int userId); // 할 일 삭제 (소유자 확인)
}