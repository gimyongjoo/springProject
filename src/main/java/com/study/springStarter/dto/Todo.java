package com.study.springStarter.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class Todo {
    private Integer todoId; // 할 일 번호
    private Integer userId; // 할 일 작성자
    private Integer noteId; // 소속 노트
    private String content; // 할 일 내용
    private Boolean isDone; // 완료 여부
    private LocalDateTime dueDate; // 마감 기한
    private LocalDateTime createDate; // 생성 일시

    public Todo(Integer todoId, Integer userId, Integer noteId, String content, Boolean isDone) {
        this.todoId = todoId;
        this.userId = userId;
        this.noteId = noteId;
        this.content = content;
        this.isDone = isDone;
    }

    public Todo() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(todoId, todo.todoId) && Objects.equals(userId, todo.userId) && Objects.equals(noteId, todo.noteId) && Objects.equals(content, todo.content) && Objects.equals(isDone, todo.isDone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todoId, userId, noteId, content, isDone);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "todoId=" + todoId +
                ", userId=" + userId +
                ", noteId=" + noteId +
                ", content='" + content + '\'' +
                ", isDone=" + isDone +
                ", dueDate=" + dueDate +
                ", createDate=" + createDate +
                '}';
    }

    public Integer getTodoId() {
        return todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
