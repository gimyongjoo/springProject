package com.study.springStarter.service;

import com.study.springStarter.dto.Todo;
import com.study.springStarter.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoMapper todoMapper;

    @Autowired
    public TodoServiceImpl(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    @Override
    public int addTodo(Todo todo) throws Exception {
        return todoMapper.insertTodo(todo);
    }

    @Override
    public List<Todo> findTodosByNoteId(Integer noteId, int userId) throws Exception {
        return todoMapper.selectTodosByNoteId(noteId, userId);
    }

    @Override
    public Todo findTodoById(int todoId) throws Exception {
        return todoMapper.selectTodoById(todoId);
    }

    @Override
    public int updateTodo(Todo todo) throws Exception {
        return todoMapper.updateTodo(todo);
    }

    @Override
    public int deleteTodo(int todoId, int userId) throws Exception {
        return todoMapper.deleteTodo(todoId, userId);
    }

    @Override
    public int toggleTodoDone(int todoId, int userId) throws Exception {
        Todo todo = todoMapper.selectTodoById(todoId);
        if (todo == null) {
            throw new IllegalArgumentException("Todo를 찾을 수 없습니다.");
        }
        if (todo.getUserId() != userId) {
            throw new IllegalAccessException("해당 Todo에 대한 권한이 없습니다.");
        }
        todo.setDone(!todo.isDone());
        return todoMapper.updateTodo(todo);
    }
}