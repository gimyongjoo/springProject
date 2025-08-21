package com.study.springStarter.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class BoardDto {
    private Integer bno;
    private String title;
    private String content;
    private String writer;
    private int viewCnt;
    private int commentCnt;
    private LocalDateTime regDate;
    private LocalDateTime modiDate;

    public BoardDto() {
    }

    public BoardDto(Integer bno, String title, String content, String writer, int viewCnt, int commentCnt, LocalDateTime regDate, LocalDateTime modiDate) {
        this.bno = bno;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.viewCnt = viewCnt;
        this.commentCnt = commentCnt;
        this.regDate = regDate;
        this.modiDate = modiDate;
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "bno=" + bno +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", viewCnt=" + viewCnt +
                ", commentCnt=" + commentCnt +
                ", regDate=" + regDate +
                ", modiDate=" + modiDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BoardDto boardDto = (BoardDto) o;
        return Objects.equals(bno, boardDto.bno) && Objects.equals(title, boardDto.title) && Objects.equals(content, boardDto.content) && Objects.equals(writer, boardDto.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bno, title, content, writer);
    }

    public Integer getBno() {
        return bno;
    }

    public void setBno(Integer bno) {
        this.bno = bno;
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

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public LocalDateTime getModiDate() {
        return modiDate;
    }

    public void setModiDate(LocalDateTime modiDate) {
        this.modiDate = modiDate;
    }
}
