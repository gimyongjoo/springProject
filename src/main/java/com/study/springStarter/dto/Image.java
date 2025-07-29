package com.study.springStarter.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class Image {
    private Integer imageId; // 이미지 번호
    private Integer noteId; // 소속 노트
    private String filePath; // 이미지 파일 경로
    private LocalDateTime uploadedDate; // 업로드 일시

    public Image(Integer imageId, Integer noteId, String filePath) {
        this.imageId = imageId;
        this.noteId = noteId;
        this.filePath = filePath;
    }

    public Image() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(imageId, image.imageId) && Objects.equals(noteId, image.noteId) && Objects.equals(filePath, image.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, noteId, filePath);
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", noteId=" + noteId +
                ", filePath='" + filePath + '\'' +
                ", uploadedDate=" + uploadedDate +
                '}';
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDateTime uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}
