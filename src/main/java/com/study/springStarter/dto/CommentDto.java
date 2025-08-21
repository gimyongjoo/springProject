package com.study.springStarter.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentDto {
    private Integer cno;
    private Integer bno;
    private String comment;
    private String commenter;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    public CommentDto(Integer bno, String comment, String commenter) {
        this.bno = bno;
        this.comment = comment;
        this.commenter = commenter;
    }

    public CommentDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(cno, that.cno) && Objects.equals(bno, that.bno) && Objects.equals(comment, that.comment) && Objects.equals(commenter, that.commenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cno, bno, comment, commenter);
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "cno=" + cno +
                ", bno=" + bno +
                ", comment='" + comment + '\'' +
                ", commenter='" + commenter + '\'' +
                ", regDate=" + regDate +
                ", updateDate=" + updateDate +
                '}';
    }

    public Integer getCno() {
        return cno;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
    }

    public Integer getBno() {
        return bno;
    }

    public void setBno(Integer bno) {
        this.bno = bno;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
