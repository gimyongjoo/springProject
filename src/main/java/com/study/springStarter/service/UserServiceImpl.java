package com.study.springStarter.service;

import com.study.springStarter.dto.User;
import com.study.springStarter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper mapper;

    @Override
    public int register(User user) throws Exception {
        return mapper.insert(user);
    }

    @Override
    public User findByEmail(String email) throws Exception {
        return mapper.selectByEmail(email);
    }

    @Override
    public User findById(int userId) throws Exception {
        return mapper.selectById(userId);
    }

    @Override
    public int countByEmail(String email) throws Exception {
        return mapper.countByEmail(email);
    }

    @Override
    public int countByName(String name) throws Exception {
        return mapper.countByName(name);
    }

}
