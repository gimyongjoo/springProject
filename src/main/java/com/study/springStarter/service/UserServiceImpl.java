package com.study.springStarter.service;

import com.study.springStarter.dto.User;
import com.study.springStarter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    private UserMapper mapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

        @Override
        public int register(User user) throws Exception {
            user.setPwd(passwordEncoder.encode(user.getPwd()));
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
