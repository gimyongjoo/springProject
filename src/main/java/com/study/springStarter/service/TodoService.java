package com.study.springStarter.service;

import com.study.springStarter.dto.Todo;
import java.util.List;

public interface TodoService {
    int addTodo(Todo todo) throws Exception;
    List<Todo> findTodosByNoteId(Integer noteId, int userId) throws Exception;
    Todo findTodoById(int todoId) throws Exception;
    int updateTodo(Todo todo) throws Exception;
    int deleteTodo(int todoId, int userId) throws Exception;
    int toggleTodoDone(int todoId, int userId) throws Exception;
}