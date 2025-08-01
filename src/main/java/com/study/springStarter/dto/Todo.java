package com.study.springStarter.dto;

import java.time.LocalDateTime;

public class Todo {
    private int todoId;
    private int userId;
    private Integer noteId; // noteId가 NULL을 허용하므로 Integer로 변경
    private String content;
    private boolean isDone; // isDone 컬럼과 매핑
    private LocalDateTime dueDate; // dueDate 컬럼 추가
    private LocalDateTime createDate; // createDate 컬럼과 매핑

    // Getters and Setters
    public int getTodoId() { return todoId; }
    public void setTodoId(int todoId) { this.todoId = todoId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Integer getNoteId() { return noteId; } // Integer 타입
    public void setNoteId(Integer noteId) { this.noteId = noteId; } // Integer 타입
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public boolean isDone() { return isDone; } // getter는 is 접두사
    public void setDone(boolean done) { isDone = done; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

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
}