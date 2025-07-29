package com.study.springStarter.mapper;

import com.study.springStarter.dto.User;
import org.apache.ibatis.annotations.Mapper;

public interface UserMapper {

    int insert(User user) throws Exception; // 회원가입

    User selectByEmail(String email) throws Exception; // 이메일로 조회(로그인)

    User selectById(int userId) throws Exception; // PK로 조회

    int countByEmail(String email) throws Exception; // 이메일 중복 체크

    int countByName(String name) throws Exception; // 닉네임 중복 체크

}
