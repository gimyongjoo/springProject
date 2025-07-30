package com.study.springStarter.service;

import com.study.springStarter.dto.Todo;
import com.study.springStarter.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    TodoMapper mapper;

    @Override
    public int addTodo(Todo todo) throws Exception {
        return mapper.insertTodo(todo);
    }

    @Override
    public Todo findTodoById(int todoId) throws Exception {
        return mapper.selectTodoById(todoId);
    }

    @Override
    public List<Todo> findTodosByUserId(int userId) throws Exception {
        return mapper.selectTodosByUserId(userId);
    }

    @Override
    public List<Todo> findTodosByNoteId(int noteId) throws Exception {
        return mapper.selectTodosByNoteId(noteId);
    }

    @Override
    public int updateTodo(Todo todo) throws Exception {
        return mapper.updateTodo(todo);
    }

    @Override
    public int deleteTodo(int todoId) throws Exception {
        return mapper.deleteTodo(todoId);
    }
}
