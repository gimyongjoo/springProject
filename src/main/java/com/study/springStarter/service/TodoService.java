package com.study.springStarter.service;

import com.study.springStarter.dto.Todo;

import java.util.List;

public interface TodoService {

    int addTodo(Todo todo) throws Exception;

    Todo findTodoById(int todoId) throws Exception;

    List<Todo> findTodosByUserId(int userId) throws Exception;

    List<Todo> findTodosByNoteId(int noteId) throws Exception;

    int updateTodo(Todo todo) throws Exception;

    int deleteTodo(int todoId) throws Exception;

}
