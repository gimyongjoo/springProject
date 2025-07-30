package com.study.springStarter.mapper;

import com.study.springStarter.dto.Todo;

import java.util.List;

public interface TodoMapper {

    int insertTodo(Todo todo) throws Exception; // 할 일 추가

    Todo selectTodoById(int todoId) throws Exception; // 할 일 클릭해서 상세정보 보기/수정

    List<Todo> selectTodosByUserId(int userId) throws Exception; // 내가 작성한 모든 할 일 조회

    List<Todo> selectTodosByNoteId(int noteId) throws Exception; // 노트별로 할 일만 보여주기

    int updateTodo(Todo todo) throws Exception; // 할 일 수정

    int deleteTodo(int todoId) throws Exception; // 할 일 삭제
}
