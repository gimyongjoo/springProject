package com.study.springStarter.mapper;

import com.study.springStarter.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 테스트 후 롤백하여 DB 상태를 유지합니다.
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsertUser() throws Exception {
        User user = new User();
        user.setName("newuser");
        user.setPwd("newpwd");
        user.setEmail("newuser@example.com");

        int result = userMapper.insert(user);
        Assertions.assertEquals(1, result, "사용자 삽입 성공");

        User foundUser = userMapper.selectByEmail("newuser@example.com");
        Assertions.assertNotNull(foundUser, "삽입된 사용자 조회");
        Assertions.assertEquals("newuser", foundUser.getName(), "사용자 이름 일치");
    }

    @Test
    void testSelectByEmail() throws Exception {
        // data.sql에 'testuser'가 있다고 가정
        User user = userMapper.selectByEmail("test@example.com");
        Assertions.assertNotNull(user, "이메일로 사용자 조회 성공");
        Assertions.assertEquals("testuser", user.getName(), "조회된 사용자 이름 일치");
    }

    @Test
    void testSelectById() throws Exception {
        // 먼저 사용자 삽입 후 ID로 조회
        User tempUser = new User();
        tempUser.setName("tempuser");
        tempUser.setPwd("temppwd");
        tempUser.setEmail("temp@example.com");
        userMapper.insert(tempUser); // 삽입 시 userId가 자동 생성됨

        // 삽입된 userId를 가져오는 로직이 필요 (예: User DTO에 userId가 설정되도록 Mapper XML에 <selectKey> 추가)
        // 여기서는 일단 이메일로 조회해서 userId를 가져오는 방식으로 대체
        User insertedUser = userMapper.selectByEmail("temp@example.com");
        Assertions.assertNotNull(insertedUser, "임시 사용자 삽입 및 조회 성공");

        User foundUser = userMapper.selectById(insertedUser.getUserId());
        Assertions.assertNotNull(foundUser, "ID로 사용자 조회 성공");
        Assertions.assertEquals("tempuser", foundUser.getName(), "조회된 사용자 이름 일치");
    }

    @Test
    void testCountByEmail() throws Exception {
        // data.sql에 'test@example.com'이 있다고 가정
        int count = userMapper.countByEmail("test@example.com");
        Assertions.assertEquals(1, count, "이메일 중복 체크 - 기존 이메일");

        count = userMapper.countByEmail("nonexistent@example.com");
        Assertions.assertEquals(0, count, "이메일 중복 체크 - 없는 이메일");
    }

    @Test
    void testCountByName() throws Exception {
        // data.sql에 'testuser'가 있다고 가정
        int count = userMapper.countByName("testuser");
        Assertions.assertEquals(1, count, "닉네임 중복 체크 - 기존 닉네임");

        count = userMapper.countByName("nonexistentname");
        Assertions.assertEquals(0, count, "닉네임 중복 체크 - 없는 닉네임");
    }
}