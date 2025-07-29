package com.study.springStarter.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class Folder { // 폴더/카테고리
    private Integer folderId; // 폴더 번호
    private Integer userId; // 폴더 소유자
    private String name; // 폴더명
    private LocalDateTime createdDate; // 폴더 생성일시

    public Folder(Integer folderId, Integer userId, String name) {
        this.folderId = folderId;
        this.userId = userId;
        this.name = name;
    }

    public Folder() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return Objects.equals(folderId, folder.folderId) && Objects.equals(userId, folder.userId) && Objects.equals(name, folder.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(folderId, userId, name);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "folderId=" + folderId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
