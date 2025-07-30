package com.study.springStarter.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class Note { // 노트/페이지
    private Integer noteId; // 노트 번호
    private Integer userId; // 노트 소유자
    private Integer folderId; // 소속 폴더
    private String title; // 노트 제목
    private String content; // 노트 내용
    private Boolean isPinned; // 상단 고정 여부
    private LocalDateTime createdDate; // 노트 생성일시
    private LocalDateTime updatedDate; // 마지막 수정일시
    private Boolean markdown_enabled; // 마크다운 지원 여부

    public Note(Integer noteId, Integer userId, Integer folderId, String title, String content, Boolean isPinned, Boolean markdown_enabled) {
        this.noteId = noteId;
        this.userId = userId;
        this.folderId = folderId;
        this.title = title;
        this.content = content;
        this.isPinned = isPinned;
        this.markdown_enabled = markdown_enabled;
    }

    public Note() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(noteId, note.noteId) && Objects.equals(userId, note.userId) && Objects.equals(folderId, note.folderId) && Objects.equals(title, note.title) && Objects.equals(content, note.content) && Objects.equals(isPinned, note.isPinned) && Objects.equals(markdown_enabled, note.markdown_enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, userId, folderId, title, content, isPinned, markdown_enabled);
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", userId=" + userId +
                ", folderId=" + folderId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isPinned=" + isPinned +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", markdown_enabled=" + markdown_enabled +
                '}';
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsPinned() {
        return isPinned;
    }

    public void setPinned(Boolean pinned) {
        isPinned = pinned;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getMarkdown_enabled() {
        return markdown_enabled;
    }

    public void setMarkdown_enabled(Boolean markdown_enabled) {
        this.markdown_enabled = markdown_enabled;
    }
}
