package com.study.springStarter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CheckList { // 노트 안의 체크리스트 CRUD
    private Integer checkListId; // 체크리스트 번호
    private Integer noteId; // 소속 노트
    private String content; // 체크리스트 내용
    private Boolean isChecked; // 체크 여부

    public CheckList(Integer checkListId, Integer noteId, String content, Boolean isChecked) {
        this.checkListId = checkListId;
        this.noteId = noteId;
        this.content = content;
        this.isChecked = isChecked;
    }

    public CheckList() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CheckList checkList = (CheckList) o;
        return Objects.equals(checkListId, checkList.checkListId) && Objects.equals(noteId, checkList.noteId) && Objects.equals(content, checkList.content) && Objects.equals(isChecked, checkList.isChecked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkListId, noteId, content, isChecked);
    }

    @Override
    public String toString() {
        return "CheckList{" +
                "checkListId=" + checkListId +
                ", noteId=" + noteId +
                ", content='" + content + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    public Integer getCheckListId() {
        return checkListId;
    }

    public void setCheckListId(Integer checkListId) {
        this.checkListId = checkListId;
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

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
