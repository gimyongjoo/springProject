package com.study.springStarter.service;

import com.study.springStarter.dto.User;

public interface UserService {

    int register(User user) throws Exception;

    User findByEmail(String email) throws Exception;

    User findById(int userId) throws Exception;

    int countByEmail(String email) throws Exception;

    int countByName(String name) throws Exception;

    int updatePasswordByEmail(String email, String encodedPwd) throws Exception;

}
