package com.study.springStarter.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class User { // 회원
    private Integer userId; // 사용자
    private String name; // 이름
    private String pwd; // 비밀번호
    private String email; // 이메일
    private LocalDateTime regDate; // 가입 일시

    public User() {
    }

    public User(Integer userId, String name, String pwd, String email) {
        this.userId = userId;
        this.name = name;
        this.pwd = pwd;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(name, user.name) && Objects.equals(pwd, user.pwd) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, pwd, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", eamil='" + email + '\'' +
                ", regDate=" + regDate +
                '}';
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }
}
